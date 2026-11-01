package com.example.mavuno.data

import com.example.mavuno.R


object SamplePlantData {

    // 10 disease records
    val diseases = listOf(
        DiseaseInfo(
            name = "Northern Leaf Blight",
            description = "A fungal disease affecting maize leaves.",
            symptoms = listOf(
                "Long cigar-shaped lesions",
                "Grey-green or tan leaf spots",
                "Leaves drying as damage progresses"
            ),
            causes = listOf(
                "Exserohilum turcicum fungus",
                "Humid conditions and prolonged leaf wetness"
            ),
            recommendations = listOf(
                "Grow resistant maize varieties",
                "Monitor leaves regularly",
                "Seek local agricultural advice"
            )
        ),
        DiseaseInfo(
            name = "Common Rust",
            description = "A fungal disease that produces rust-coloured pustules on maize leaves.",
            symptoms = listOf(
                "Small reddish-brown pustules",
                "Pustules scattered across leaves",
                "Yellowing around heavily affected areas"
            ),
            causes = listOf(
                "Puccinia sorghi fungus",
                "Favourable moisture and temperature conditions"
            ),
            recommendations = listOf(
                "Monitor rust development",
                "Consider resistant varieties",
                "Consult an agricultural extension officer"
            )
        ),
        DiseaseInfo(
            name = "Gray Leaf Spot",
            description = "A maize disease that causes rectangular lesions on leaves.",
            symptoms = listOf(
                "Long rectangular grey lesions",
                "Brown leaf discolouration",
                "Premature leaf drying"
            ),
            causes = listOf(
                "Cercospora fungal species",
                "Infected crop residue and humid conditions"
            ),
            recommendations = listOf(
                "Manage infected crop residue",
                "Use resistant varieties where available",
                "Inspect crops regularly"
            )
        ),
        DiseaseInfo(
            name = "Tomato Early Blight",
            description = "A fungal disease that commonly affects older tomato leaves.",
            symptoms = listOf(
                "Brown spots with concentric rings",
                "Yellowing around lesions",
                "Lower leaves drying"
            ),
            causes = listOf(
                "Alternaria fungal species",
                "Wet foliage and infected plant debris"
            ),
            recommendations = listOf(
                "Remove severely affected leaves where appropriate",
                "Avoid unnecessary leaf wetness",
                "Follow local crop protection guidance"
            )
        ),
        DiseaseInfo(
            name = "Tomato Late Blight",
            description = "A potentially destructive disease affecting tomato foliage and fruit.",
            symptoms = listOf(
                "Water-soaked leaf patches",
                "Rapidly spreading brown lesions",
                "Possible white growth in humid conditions"
            ),
            causes = listOf(
                "Phytophthora infestans",
                "Cool, wet weather conditions"
            ),
            recommendations = listOf(
                "Seek prompt agricultural assessment",
                "Avoid moving infected plant material",
                "Follow locally approved management advice"
            )
        ),
        DiseaseInfo(
            name = "Tomato Bacterial Wilt",
            description = "A bacterial disease associated with sudden wilting in susceptible tomato plants.",
            symptoms = listOf(
                "Sudden wilting",
                "Leaves may remain green initially",
                "Progressive plant collapse"
            ),
            causes = listOf(
                "Ralstonia solanacearum species complex",
                "Contaminated soil or planting material"
            ),
            recommendations = listOf(
                "Seek confirmation before treatment",
                "Use clean planting material",
                "Follow crop rotation and sanitation guidance"
            )
        ),
        DiseaseInfo(
            name = "Bean Rust",
            description = "A fungal disease that causes rust-coloured spots on bean leaves.",
            symptoms = listOf(
                "Orange-brown pustules",
                "Yellow leaf areas",
                "Premature leaf drop in severe cases"
            ),
            causes = listOf(
                "Uromyces appendiculatus fungus",
                "Favourable moisture conditions"
            ),
            recommendations = listOf(
                "Monitor leaves for new pustules",
                "Use resistant varieties where available",
                "Seek local management advice"
            )
        ),
        DiseaseInfo(
            name = "Cassava Mosaic Disease",
            description = "A viral disease that can cause mosaic patterns and distorted cassava leaves.",
            symptoms = listOf(
                "Yellow and green mosaic patterns",
                "Leaf distortion",
                "Reduced plant growth"
            ),
            causes = listOf(
                "Cassava mosaic begomoviruses",
                "Infected cuttings and whitefly transmission"
            ),
            recommendations = listOf(
                "Use certified healthy planting material",
                "Remove infected plants following local guidance",
                "Choose resistant varieties where available"
            )
        ),
        DiseaseInfo(
            name = "Potato Late Blight",
            description = "A disease that can rapidly damage potato foliage and tubers.",
            symptoms = listOf(
                "Dark water-soaked leaf patches",
                "Brown or black lesions",
                "Possible tuber decay"
            ),
            causes = listOf(
                "Phytophthora infestans",
                "Cool, wet weather and infected plant material"
            ),
            recommendations = listOf(
                "Monitor crops frequently",
                "Use healthy certified seed potatoes",
                "Follow local disease management guidance"
            )
        ),
        DiseaseInfo(
            name = "Groundnut Leaf Spot",
            description = "A fungal disease that produces spots on groundnut leaves.",
            symptoms = listOf(
                "Brown or dark leaf spots",
                "Yellowing around lesions",
                "Premature leaf loss"
            ),
            causes = listOf(
                "Cercospora or related fungal species",
                "Moist conditions and infected crop debris"
            ),
            recommendations = listOf(
                "Monitor leaf development",
                "Manage crop residue appropriately",
                "Seek advice on suitable resistant varieties"
            )
        )
    )

    // 10 plant scans
    val scans = listOf(
        PlantScan(
            id = 1,
            plantName = "Maize",
            plantType = "Maize - Leaf",
            imageUrl = R.drawable.maize_diseased_1,
            scanDate = "27 Oct 2026",
            status = "Diseased",
            healthLabel = "Northern Leaf Blight",
            confidence = 0.94f,
            disease = diseases[0]
        ),
        PlantScan(
            id = 2,
            plantName = "Maize",
            plantType = "Maize - Leaf",
            imageUrl = R.drawable.maize_healthy_1,
            scanDate = "26 Oct 2026",
            status = "Healthy",
            healthLabel = "Looking healthy!",
            confidence = 0.97f
        ),
        PlantScan(
            id = 3,
            plantName = "Maize",
            plantType = "Maize - Leaf",
            imageUrl = R.drawable.maize_diseased_2,
            scanDate = "25 Oct 2026",
            status = "Diseased",
            healthLabel = "Common Rust",
            confidence = 0.91f,
            disease = diseases[1]
        ),
        PlantScan(
            id = 4,
            plantName = "Tomato",
            plantType = "Tomato - Leaf",
            imageUrl = R.drawable.tomato_healthy_1,
            scanDate = "24 Oct 2026",
            status = "Healthy",
            healthLabel = "Looking healthy!",
            confidence = 0.96f
        ),
        PlantScan(
            id = 5,
            plantName = "Maize",
            plantType = "Maize - Leaf",
            imageUrl = R.drawable.maize_diseased_3,
            scanDate = "23 Oct 2026",
            status = "Diseased",
            healthLabel = "Gray Leaf Spot",
            confidence = 0.88f,
            disease = diseases[2]
        ),
        PlantScan(
            id = 6,
            plantName = "Tomato",
            plantType = "Tomato - Leaf",
            imageUrl = R.drawable.tomato_diseased_1,
            scanDate = "22 Oct 2026",
            status = "Diseased",
            healthLabel = "Early Blight",
            confidence = 0.93f,
            disease = diseases[3]
        ),
        PlantScan(
            id = 7,
            plantName = "Tomato",
            plantType = "Tomato - Leaf",
            imageUrl = R.drawable.tomato_diseased_2,
            scanDate = "21 Oct 2026",
            status = "Diseased",
            healthLabel = "Late Blight",
            confidence = 0.89f,
            disease = diseases[4]
        ),
        PlantScan(
            id = 8,
            plantName = "Beans",
            plantType = "Bean - Leaf",
            imageUrl = R.drawable.bean_healthy_1,
            scanDate = "20 Oct 2026",
            status = "Healthy",
            healthLabel = "Looking healthy!",
            confidence = 0.98f
        ),
        PlantScan(
            id = 9,
            plantName = "Cassava",
            plantType = "Cassava - Leaf",
            imageUrl = R.drawable.cassava_diseased_1,
            scanDate = "19 Oct 2026",
            status = "Diseased",
            healthLabel = "Cassava Mosaic Disease",
            confidence = 0.90f,
            disease = diseases[7]
        ),
        PlantScan(
            id = 10,
            plantName = "Potato",
            plantType = "Potato - Leaf",
            imageUrl = R.drawable.potato_healthy_1,
            scanDate = "18 Oct 2026",
            status = "Healthy",
            healthLabel = "Looking healthy!",
            confidence = 0.95f
        )
    )

    // 10 care tips
    val careTips = listOf(
        CareTip(
            id = 1,
            title = "Keep your maize thriving",
            description = "Inspect leaves regularly for spots and unusual discolouration.",
            category = "Monitoring"
        ),
        CareTip(
            id = 2,
            title = "Water wisely",
            description = "Adjust watering to crop needs and avoid prolonged leaf wetness.",
            category = "Watering"
        ),
        CareTip(
            id = 3,
            title = "Check your crops regularly",
            description = "Look for early changes in leaf colour, texture and growth.",
            category = "Prevention"
        ),
        CareTip(
            id = 4,
            title = "Choose healthy seeds",
            description = "Obtain quality seeds and planting materials from reliable suppliers.",
            category = "Planting"
        ),
        CareTip(
            id = 5,
            title = "Keep your field clean",
            description = "Manage diseased crop residue appropriately to reduce disease carryover.",
            category = "Field hygiene"
        ),
        CareTip(
            id = 6,
            title = "Improve plant spacing",
            description = "Use recommended spacing to support airflow through the crop canopy.",
            category = "Prevention"
        ),
        CareTip(
            id = 7,
            title = "Monitor soil health",
            description = "Check soil conditions and use soil-test results to guide nutrient management.",
            category = "Soil care"
        ),
        CareTip(
            id = 8,
            title = "Watch for pests",
            description = "Inspect leaves for insect damage and signs of pest activity.",
            category = "Pest control"
        ),
        CareTip(
            id = 9,
            title = "Rotate your crops",
            description = "Use suitable crop rotations to help manage diseases that survive in crop residue or soil.",
            category = "Crop rotation"
        ),
        CareTip(
            id = 10,
            title = "Track plant progress",
            description = "Record observations and compare new growth over time.",
            category = "Monitoring"
        )
    )
}
