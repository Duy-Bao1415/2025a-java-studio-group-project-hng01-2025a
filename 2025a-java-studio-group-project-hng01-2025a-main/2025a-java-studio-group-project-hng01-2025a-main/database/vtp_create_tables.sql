-- Create tables for Semester 1 2023 CTG example ER Model
PRAGMA foreign_keys = OFF;
drop table if exists LGA;
drop table if exists Population;
PRAGMA foreign_keys = ON;

CREATE TABLE LGA (
    code INTEGER NOT NULL,
    year INTEGER NOT NULL,
    name TEXT NOT NULL,
    area DOUBLE,
    latitude DOUBLE,
    longitude DOUBLE,
    PRIMARY KEY (code, year)
);

// data on Indigenous status by sex and age (2016 & 2021)
CREATE TABLE Population (
    lga_code INTEGER NOT NULL,
    lga_year INTEGER NOT NULL,
    indigenous_status TEXT NOT NULL,
    sex CHAR(1) NOT NULL,
    age_category TEXT NOT NULL,
    count INTEGER NOT NULL,
    age_min INTEGER,
    age_max INTEGER,
    PRIMARY KEY (lga_code, lga_year, indigenous_status, sex, age_category),
    FOREIGN KEY (lga_code, lga_year) REFERENCES LGA(code, year)
);

-- data on lga_non_school_education_by_indigenous _status_by_sex_census 
-- số lượng sinh viên hoàn thành các cấp học ví dụ như số lượng sinh viên hoàn thành bậc đại học ... 
CREATE TABLE Education_level (
    lga_code INTEGER NOT NULL,
    lga_year INTEGER NOT NULL,
    indigenous_status TEXT NOT NULL,
    sex CHAR(1) NOT NULL,
    Category VARCHAR(50) NOT NULL ,
    count INTEGER NOT NULL,
    PRIMARY KEY (lga_code, lga_year, indigenous_status, sex, Category), 
    FOREIGN KEY (lga_code, lga_year) REFERENCES LGA(code, year)
);

-- data của lga_highest_year_of_school_completed_by_indigenous_status_by_Sex 
-- số lượng sinh viên hoàn thành cấp học cao nhất và số lượng học sinh không đi học 
CREATE TABLE School_completion (
    lga_code INTEGER NOT NULL,
    lga_year INTEGER NOT NULL,
    indigenous_status TEXT NOT NULL,
    sex CHAR(1) NOT NULL,
    Category VARCHAR(50) NOT NULL ,
    count INTEGER NOT NULL,
    PRIMARY KEY (lga_code, lga_year, indigenous_status, sex, Category), 
    FOREIGN KEY (lga_code, lga_year) REFERENCES LGA(code, year)
);

-- data của Long_term_health_condition_statics_by_indigenous_status_and_sex
-- số lượng người bị các bệnh 

CREATE TABLE Health (
    lga_code INTEGER NOT NULL,
    lga_year INTEGER NOT NULL,
    indigenous_status TEXT NOT NULL,
    sex CHAR(1) NOT NULL,
    Category VARCHAR(50) NOT NULL ,
    count INTEGER NOT NULL,
    PRIMARY KEY (lga_code, lga_year, indigenous_status, sex, Category), 
    FOREIGN KEY (lga_code, lga_year) REFERENCES LGA(code, year)
);