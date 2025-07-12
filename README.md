# Closing The Gap Data Analysis Project

This project provides a web-based interface for analyzing indigenous data including education, health, and population statistics with interactive filtering and gap analysis tools.

This implementation provides:

* Interactive data visualization for age, health, and education statistics
* Gap analysis tools for comparing datasets across years
* Advanced filtering system with multiple criteria
* Responsive design for all device sizes
* Dynamic data tables with sorting capabilities

Project Structure:
```bash
├── /src/main
│    ├── java
│    │    ├── app
│    │    │    ├── dataLogic                    - Data processing logic
│    │    │    │   ├── ageClass.java             - Handles age-related data calculations
│    │    │    │   ├── Database                  - Provide data for the website
│    │    │    │   ├── EducationClass.java       - Manages education data processing
│    │    │    │   ├── gapanalysisClass.java     - Contains logic to compare data and find service gaps
│    │    │    │   ├── healthClass.java          - Handles health-related data processing
│    │    │    │   ├── JDBCmain.java             - Main JDBC data import/execution class
│    │    │    │   ├── Level_1.java              - Level 1 data logic
│    │    │    │   ├── Level_2.java              - Level 2 data logic
│    │    │    │   ├── Level_3.java              - Level 3 data logic
│    │    │    │   ├── memberClass.java          - Models or manages member/user information
│    │    │    │   ├── personaClass.java         - Contains data models and logic for personas
│    │    │    │   ├── populationClass.java      - Processes population-related datasets
│    │    │    │   └── school_CompletionClass.java - Handles school completion statistics
│    │    │    ├── App.java                      - Main application entry point
│    │    │    ├── JDBCConnection.java           - Database connection handler
│    │    │    ├── gapanalysis_Controller.java   - Controller for gap analysis web page
│    │    │    ├── PageIndex.java                - Homepage controller
│    │    │    ├── PageVision.java               - Vision page controller
│    │    │    ├── PageMission.java              - Mission statement controller
│    │    │    ├── PageAgeHealth.java            - Age & Health controller
│    │    │    ├── PageEducation.java            - Education controller
│    │    │    ├── PageHow2Use.java              - Usage guide controller
│    │    │    ├── PagePersona.java              - Personas page controller
│    │    │    └── PageContact.java              - Contact page controller
│    │    │
│    │    └── helper                             - Data conversion utilities (CSV to DB)
│    │        ├── Education_completion           - Converts school completion data to database entries
│    │        ├── Education_level                - Converts education level data to database format
│    │        ├── Health                         - Converts health condition data for DB usage
│    │        ├── LGA                            - Converts Local Government Area data to DB format
│    │        ├── MainDatabase                   - Core utility to orchestrate DB table creations and inserts
│    │        └── Population                     - Converts population datasets to database entries
│    │
│    └── resources
│         ├── templates
│         │    ├── index.html                    - Homepage
│         │    ├── vision.html                   - Vision page
│         │    ├── missionstatement.html         - Mission statement page
│         │    ├── agehealth.html                - Age & Health statistics page
│         │    ├── education.html                - Education statistics page
│         │    ├── gapanalysis.html              - Gap analysis statistics page
│         │    ├── how2use.html                  - Usage guide
│         │    ├── persona.html                  - Personas page
│         │    ├── contact.html                  - Contact page
│         │    ├── signin.html                   - Sign in page
│         │    └── signup.html                   - Sign up page
│         │
│         └── static
│              ├── css
│              │    ├── agehealth.css            - Age & Health styles
│              │    ├── education.css            - Education styles
│              │    ├── gapanalysis.css          - Gap analysis styles
│              │    ├── missionstatement.css     - Mission page styles
│              │    ├── how2use.css              - Usage guide styles
│              │    ├── persona.css              - Personas page styles
│              │    ├── contact.css              - Contact page styles
│              │    └── common.css               - Shared/global styles
│              │
│              └── images                        - Shared images for UI (e.g., icons, headers)
│
├── database
│    ├── ctg.db                                  - SQLite database file for the application
│    ├── lga_2016.csv                            - LGA statistics for 2016
│    ├── lga_2021.csv                            - LGA statistics for 2021
│    ├── indigenous_status_by_age_by_sex_2016.csv - Age breakdown by gender & status (2016)
│    ├── indigenous_status_by_age_by_sex_2021.csv - Age breakdown by gender & status (2021)
│    ├── education_level_2016.csv                - Education attainment data (2016)
│    ├── education_level_2021.csv                - Education attainment data (2021)
│    ├── health_conditions_2016.csv              - Health condition data (2016)
│    ├── health_conditions_2021.csv              - Health condition data (2021)
│    └── vtp_create_tables.sql                   - SQL script to create required database tables
│
├── pom.xml                                      - Maven configuration (Do not configure)
└── README.md                                    - Project documentation and instructions

```

Current Libraries:
* org.xerial.sqlite-jdbc (SQLite JDBC library)
* javalin (lightweight Java Webserver)
* thymeleaf (HTML template)

Required Dependencies:
* slf4j-simple (logging)
* sqlite-jdbc (database)

## Running the Application

1. Clone the repository
2. Open in VSCode
3. Install dependencies:
   - Allow VSCode to configure build
   - Download required Java libraries
4. Run the application:
   - Open `src/main/java/app/App.java`
   - Click "Run" above main function
5. Access at: http://localhost:7001

## Key Features

* Interactive Data Tables
  - Sortable columns
  - Custom scrolling
  - Fixed headers
  - Dynamic loading

* Advanced Filtering
  - Age range selection
  - Geographic filtering
  - Status filtering
  - Category filtering

* Gap Analysis
  - Year-over-year comparison
  - Population trends
  - Education gaps
  - Health disparities

## Authors
Team HN-G01-JavaStudio-2025A:
* Uong Minh Duc
* Tran Chi Thien
* Luong Thuy Vy
* Pham Duy Bao
* Nguyen Hoang Tung

Copyright RMIT University (c) 2025
