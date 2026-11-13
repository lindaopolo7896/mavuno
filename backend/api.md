# Mavuno API v1

Base URL: `https://<host>/api/v1/`. Locally: `http://10.0.2.2:8000/api/v1/` from the Android emulator.

- Requests and responses are JSON, except photo uploads, which are `multipart/form-data`.
- All URLs end with a trailing slash (`/scans/`, not `/scans`).
- Dates are ISO 8601 with a timezone offset, e.g. `2026-11-13T23:45:19.582463+03:00`.
- IDs: users have integer IDs, scans have UUIDs.

## Contents

- [Authentication](#authentication)
- [Errors](#errors)
- [Auth endpoints](#auth-endpoints): register, login, refresh, logout
- [Profile](#profile): get, update, delete account
- [Scans](#scans): diagnose a photo, history, detail, delete

## Authentication

Register or log in to get two tokens:

| Token | Lifetime | Use |
|---|---|---|
| `access` | 30 minutes | Sent on every request: `Authorization: Bearer <access>` |
| `refresh` | 30 days | Only used to get a new access token, and to log out |

- An expired or invalid access token returns `401` with `"code": "token_not_valid"`.
- [`POST /auth/token/refresh/`](#refresh-token) returns a new access token **and a new refresh token**.
  The previous refresh token is blacklisted.
- A refresh token that is expired, blacklisted or logged out returns `401` from the refresh endpoint.

## Errors

| Status | Meaning | Body |
|---|---|---|
| `400` | Invalid input | Field errors: `{"email": ["An account with this email already exists."]}`. Errors not tied to one field use `non_field_errors` |
| `401` | Missing, invalid or expired token, or wrong login | `{"detail": "...", "code": "token_not_valid"}` (the `code` is present for token problems) |
| `404` | Not found, or the scan belongs to another user | `{"detail": "No Scan matches the given query."}` |
| `422` | Photo not recognised confidently (predict only) | See [Diagnose a photo](#diagnose-a-photo) |
| `429` | Too many login/register attempts | `{"detail": "Request was throttled. Expected available in 42 seconds."}` |
| `503` | Diagnosis temporarily unavailable | `{"detail": "Scanning is temporarily unavailable."}` |

Field error values are always lists of strings.

---

## Auth endpoints

### Register

`POST /auth/register/`. No token needed.

```json
{
  "email": "farmer@example.com",
  "password": "Sh4mba-strong!",
  "full_name": "Jane Wanjiru",
  "phone_number": "+254700000000"
}
```

`full_name` and `phone_number` are optional. Emails are case-insensitive and stored in lowercase.
Passwords must be at least 8 characters, not too common, not entirely numeric, and not too similar to the email.

**`201 Created`**

```json
{
  "user": {
    "id": 1,
    "email": "farmer@example.com",
    "full_name": "Jane Wanjiru",
    "phone_number": "+254700000000",
    "date_joined": "2026-11-13T23:10:14.679307+03:00"
  },
  "refresh": "eyJhbGciOi...",
  "access": "eyJhbGciOi..."
}
```

The tokens are valid immediately.

**Errors:** `400` `{"email": ["An account with this email already exists."]}`,
`400` `{"non_field_errors": ["This password is too short. It must contain at least 8 characters."]}`, `429`.

### Login

`POST /auth/login/`. No token needed.

```json
{ "email": "farmer@example.com", "password": "Sh4mba-strong!" }
```

**`200 OK`**: same shape as register (`user`, `refresh`, `access`).

**Errors:** `401` `{"detail": "No active account found with the given credentials"}`, `429` after 10 attempts per minute.

### Refresh token

`POST /auth/token/refresh/`. No access token needed.

```json
{ "refresh": "eyJhbGciOi..." }
```

**`200 OK`**

```json
{ "access": "eyJhbGciOi...", "refresh": "eyJhbGciOi..." }
```

The refresh token sent is blacklisted.

**Errors:** `401` `{"detail": "Token is blacklisted", "code": "token_not_valid"}`, or a similar `401` for expired/invalid tokens.

### Logout

`POST /auth/logout/`. Requires access token.

```json
{ "refresh": "eyJhbGciOi..." }
```

**`204 No Content`**. The refresh token is blacklisted. The access token stays valid until it expires.

**Errors:** `400` `{"detail": "Invalid or expired refresh token."}`.

---

## Profile

### Get profile

`GET /users/me/`. Requires access token.

**`200 OK`**

```json
{
  "id": 1,
  "email": "farmer@example.com",
  "full_name": "Jane Wanjiru",
  "phone_number": "+254700000000",
  "date_joined": "2026-11-13T23:10:14.679307+03:00"
}
```

### Update profile

`PATCH /users/me/`. Requires access token. Send only the fields to change.

```json
{ "full_name": "Jane W.", "phone_number": "+254711111111" }
```

**`200 OK`**: the updated profile. `email`, `id` and `date_joined` cannot be changed and are ignored if sent.

### Delete account

`DELETE /users/me/`. Requires access token. No body.

**`204 No Content`**. The account, all its scans and their photos are permanently deleted.

---

## Scans

### Scan object

Returned by every scan endpoint.

```json
{
  "id": "0938a21b-2c9b-4024-8474-58f7511b4269",
  "image": "https://<host>/media/scans/2026/11/38b1faff95fa4f9c85aad2a366cf80f0.jpg",
  "plant_name": "Tomato",
  "disease": "Early blight",
  "confidence": 0.9993,
  "is_healthy": false,
  "predictions": [
    { "label": "Tomato___Early_blight", "plant_name": "Tomato", "disease": "Early blight", "is_healthy": false, "confidence": 0.9993 },
    { "label": "Tomato___Late_blight", "plant_name": "Tomato", "disease": "Late blight", "is_healthy": false, "confidence": 0.0007 },
    { "label": "Potato___Early_blight", "plant_name": "Potato", "disease": "Early blight", "is_healthy": false, "confidence": 0.0 }
  ],
  "scanned_at": "2026-11-12T10:00:00+03:00",
  "created_at": "2026-11-13T23:45:19.582463+03:00"
}
```

| Field | Type | Notes |
|---|---|---|
| `id` | UUID string | |
| `image` | URL or `null` | Full URL of the uploaded photo |
| `plant_name` | string | e.g. `Tomato`, `Maize`, `Bell pepper` |
| `disease` | string | Empty string when the plant is healthy |
| `confidence` | number 0–1 | Confidence of the top result |
| `is_healthy` | boolean | |
| `predictions` | list | Top 3 results, best first. `label` is the raw class name |
| `scanned_at` | datetime | When the photo was taken (defaults to upload time) |
| `created_at` | datetime | When the scan was saved |

### Diagnose a photo

`POST /scans/predict/`. Requires access token. **`multipart/form-data`**.

| Part | Required | Notes |
|---|---|---|
| `image` | yes | JPEG/PNG, max 10 MB |
| `scanned_at` | no | ISO datetime of when the photo was taken |

```bash
curl -X POST https://<host>/api/v1/scans/predict/ \
  -H "Authorization: Bearer $ACCESS" \
  -F "image=@leaf.jpg" -F "scanned_at=2026-11-12T07:00:00Z"
```

**`201 Created`**: the saved [scan object](#scan-object). It now appears in the history.

**`422 Unprocessable Entity`**: the photo wasn't recognised confidently, e.g. it isn't a leaf or
it's a crop the model doesn't know. **Nothing is saved.** `predictions` holds the model's top guesses.

```json
{
  "code": "low_confidence",
  "detail": "We couldn't identify this plant clearly. Take a close, well-lit photo of a single leaf.",
  "predictions": [
    { "label": "Corn_(maize)___Common_rust_", "plant_name": "Maize", "disease": "Common rust", "is_healthy": false, "confidence": 0.6147 }
  ]
}
```

**Other errors:** `400` `{"image": ["Upload a valid image. ..."]}` or `{"image": ["Image must be 10 MB or smaller."]}`, `503`.

Supported crops: Apple, Blueberry, Cherry, Maize, Grape, Orange, Peach, Bell pepper, Potato,
Raspberry, Soybean, Squash, Strawberry, Tomato.

### Scan history

`GET /scans/?page=1`. Requires access token. Newest first (by `scanned_at`), 20 per page.

**`200 OK`**

```json
{
  "count": 42,
  "next": "https://<host>/api/v1/scans/?page=2",
  "previous": null,
  "results": [ { "...scan object..." } ]
}
```

`next` is `null` on the last page. Only the authenticated user's scans are returned.

### Scan detail

`GET /scans/<id>/`. Requires access token.

**`200 OK`**: a [scan object](#scan-object). **`404`** if it doesn't exist or belongs to another user.

### Delete a scan

`DELETE /scans/<id>/`. Requires access token.

**`204 No Content`**. The scan and its photo are deleted.

### Save a scan without diagnosis

`POST /scans/`. Requires access token. Saves a result that was produced outside the server
(e.g. on-device). [`/scans/predict/`](#diagnose-a-photo) runs the model and saves in one step.

Accepts JSON, or multipart when including `image`. All fields are optional: `image`, `plant_name`,
`disease`, `confidence` (0–1), `is_healthy`, `predictions` (list of objects with at least `label`;
in multipart, send it as a JSON string), `scanned_at`.

**`201 Created`**: the saved [scan object](#scan-object).

