"""The 38 classes the model predicts.

ORDER MATTERS: the model outputs 38 scores and score i belongs to CLASSES[i].
This is the training order from ResNet18_Base.ipynb: sorted(color_train["class"].unique()).
Do not reorder or edit this list unless the model is retrained.

Each entry is (label from training, plant name, disease name or None if healthy).
"""

CLASSES = [
    ("Apple___Apple_scab", "Apple", "Apple scab"),
    ("Apple___Black_rot", "Apple", "Black rot"),
    ("Apple___Cedar_apple_rust", "Apple", "Cedar apple rust"),
    ("Apple___healthy", "Apple", None),
    ("Blueberry___healthy", "Blueberry", None),
    ("Cherry_(including_sour)___Powdery_mildew", "Cherry", "Powdery mildew"),
    ("Cherry_(including_sour)___healthy", "Cherry", None),
    ("Corn_(maize)___Cercospora_leaf_spot Gray_leaf_spot", "Maize", "Gray leaf spot (Cercospora)"),
    ("Corn_(maize)___Common_rust_", "Maize", "Common rust"),
    ("Corn_(maize)___Northern_Leaf_Blight", "Maize", "Northern leaf blight"),
    ("Corn_(maize)___healthy", "Maize", None),
    ("Grape___Black_rot", "Grape", "Black rot"),
    ("Grape___Esca_(Black_Measles)", "Grape", "Esca (black measles)"),
    ("Grape___Leaf_blight_(Isariopsis_Leaf_Spot)", "Grape", "Leaf blight (Isariopsis leaf spot)"),
    ("Grape___healthy", "Grape", None),
    ("Orange___Haunglongbing_(Citrus_greening)", "Orange", "Huanglongbing (citrus greening)"),
    ("Peach___Bacterial_spot", "Peach", "Bacterial spot"),
    ("Peach___healthy", "Peach", None),
    ("Pepper,_bell___Bacterial_spot", "Bell pepper", "Bacterial spot"),
    ("Pepper,_bell___healthy", "Bell pepper", None),
    ("Potato___Early_blight", "Potato", "Early blight"),
    ("Potato___Late_blight", "Potato", "Late blight"),
    ("Potato___healthy", "Potato", None),
    ("Raspberry___healthy", "Raspberry", None),
    ("Soybean___healthy", "Soybean", None),
    ("Squash___Powdery_mildew", "Squash", "Powdery mildew"),
    ("Strawberry___Leaf_scorch", "Strawberry", "Leaf scorch"),
    ("Strawberry___healthy", "Strawberry", None),
    ("Tomato___Bacterial_spot", "Tomato", "Bacterial spot"),
    ("Tomato___Early_blight", "Tomato", "Early blight"),
    ("Tomato___Late_blight", "Tomato", "Late blight"),
    ("Tomato___Leaf_Mold", "Tomato", "Leaf mold"),
    ("Tomato___Septoria_leaf_spot", "Tomato", "Septoria leaf spot"),
    ("Tomato___Spider_mites Two-spotted_spider_mite", "Tomato", "Spider mites (two-spotted spider mite)"),
    ("Tomato___Target_Spot", "Tomato", "Target spot"),
    ("Tomato___Tomato_Yellow_Leaf_Curl_Virus", "Tomato", "Tomato yellow leaf curl virus"),
    ("Tomato___Tomato_mosaic_virus", "Tomato", "Tomato mosaic virus"),
    ("Tomato___healthy", "Tomato", None),
]

assert len(CLASSES) == 38
