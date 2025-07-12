package app.dataLogic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import helper.Education_completion;
import helper.Education_level;

public class Level_3 extends JDBCmain {

    public Map<String, String> getLGACodes() {
        Map<String, String> lgaCodes = new HashMap<>();
        try {
            Connection connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = 
                "SELECT DISTINCT code, name " +
                "FROM lga " +
                "WHERE year = 2021 " +  // Using 2021 as it's the most recent year
                "ORDER BY name;";

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                String code = results.getString("code");
                String name = results.getString("name");
                lgaCodes.put(code, name);
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return lgaCodes;
    }

    public ArrayList<gapanalysisClass> getGapAnalysis(String lga_code, String age_min, String age_max, 
            List<String> sexes, List<String> statuses, List<String> areas, List<String> years) {
        ArrayList<gapanalysisClass> gapAnalysis = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // Build the WHERE clause based on filters
            StringBuilder whereClause = new StringBuilder();
            List<String> conditions = new ArrayList<>();

            // LGA code condition
            if (lga_code != null && !lga_code.trim().isEmpty()) {
                conditions.add("lga_code = '" + lga_code + "'");
            }

            // Age range conditions
            if (age_min != null && !age_min.trim().isEmpty()) {
                conditions.add("age_min >= " + age_min);
            }
            if (age_max != null && !age_max.trim().isEmpty()) {
                conditions.add("age_max <= " + age_max);
            }

            // Sex filter
            if (sexes != null && !sexes.isEmpty()) {
                String sexCondition = "sex IN ('" + String.join("','", sexes) + "')";
                conditions.add(sexCondition);
            }

            // Status filter
            if (statuses != null && !statuses.isEmpty()) {
                List<String> mappedStatuses = new ArrayList<>();
                for (String status : statuses) {
                    switch (status.toLowerCase()) {
                        case "indigenous":
                            mappedStatuses.add("'indig'");
                            break;
                        case "non-indigenous":
                            mappedStatuses.add("'non_indig'");
                            break;
                        case "not-stated":
                            mappedStatuses.add("'indig_stat_notstated'");
                            break;
                    }
                }
                if (!mappedStatuses.isEmpty()) {
                    conditions.add("indigenous_status IN (" + String.join(",", mappedStatuses) + ")");
                }
            }

            // Area filter
            if (areas != null && !areas.isEmpty()) {
                List<String> stateCodes = new ArrayList<>();
                for (String area : areas) {
                    switch (area.toLowerCase()) {
                        case "new-south-wales":
                            stateCodes.add("'1'");
                            break;
                        case "victoria":
                            stateCodes.add("'2'");
                            break;
                        case "queensland":
                            stateCodes.add("'3'");
                            break;
                        case "south-australia":
                            stateCodes.add("'4'");
                            break;
                        case "western-australia":
                            stateCodes.add("'5'");
                            break;
                        case "tasmania":
                            stateCodes.add("'6'");
                            break;
                        case "northern-territory":
                            stateCodes.add("'7'");
                            break;
                        case "act":
                            stateCodes.add("'8'");
                            break;
                    }
                }
                if (!stateCodes.isEmpty()) {
                    conditions.add("lga.state_code IN (" + String.join(",", stateCodes) + ")");
                }
            }

            // Year filter
            if (years != null && !years.isEmpty()) {
                conditions.add("lga_year IN (" + String.join(",", years) + ")");
            }

            // Combine all conditions
            if (!conditions.isEmpty()) {
                whereClause.append("WHERE ").append(String.join(" AND ", conditions));
            }

            String query = 
            "WITH Filtered AS (\r\n" +
            "    SELECT \r\n" +
            "        p.lga_code, \r\n" +
            "        p.lga_year, \r\n" +
            "        p.sex, \r\n" +
            "        p.indigenous_status, \r\n" +
            "        SUM(p.count) as count \r\n" +
            "    FROM Population p \r\n" +
            "    JOIN lga ON p.lga_code = lga.code AND p.lga_year = lga.year \r\n" +
            "    " + whereClause.toString() + " \r\n" +
            "    GROUP BY p.lga_code, p.lga_year, p.sex, p.indigenous_status \r\n" +
            "), \r\n" +
            "Pivoted AS (\r\n" +
            "    SELECT \r\n" +
            "        lga.name AS lga_name, \r\n" +
            "        lga.code AS lga_code, \r\n" +
            "        CASE \r\n" +
            "            WHEN f.sex = 'm' THEN 'Male' \r\n" +
            "            WHEN f.sex = 'f' THEN 'Female' \r\n" +
            "            ELSE 'Unknown' \r\n" +
            "        END AS sex, \r\n" +
            "        CASE \r\n" +
            "            WHEN f.indigenous_status = 'indig' THEN 'Indigenous' \r\n" +
            "            WHEN f.indigenous_status = 'non_indig' THEN 'Non-Indigenous' \r\n" +
            "            WHEN f.indigenous_status = 'indig_stat_notstated' THEN 'Not Stated' \r\n" +
            "            ELSE 'Unknown' \r\n" +
            "        END AS indigenous_status, \r\n" +
            "        MAX(CASE WHEN f.lga_year = 2016 THEN f.count ELSE 0 END) AS total_2016, \r\n" +
            "        MAX(CASE WHEN f.lga_year = 2021 THEN f.count ELSE 0 END) AS total_2021 \r\n" +
            "    FROM Filtered f \r\n" +
            "    JOIN lga ON f.lga_code = lga.code AND f.lga_year = lga.year \r\n" +
            "    GROUP BY lga.name, lga.code, f.sex, f.indigenous_status \r\n" +
            ") \r\n" +
            "SELECT \r\n" +
            "    p.lga_name, \r\n" +
            "    p.lga_code, \r\n" +
            "    p.sex, \r\n" +
            "    p.indigenous_status, \r\n" +
            "    p.total_2016, \r\n" +
            "    p.total_2021, \r\n" +
            "    p.total_2021 - p.total_2016 AS change, \r\n" +
            "    CASE \r\n" +
            "        WHEN p.total_2021 - p.total_2016 > 0 THEN 'Increased' \r\n" +
            "        WHEN p.total_2021 - p.total_2016 < 0 THEN 'Decreased' \r\n" +
            "        ELSE 'No Change' \r\n" +
            "    END AS status \r\n" +
            "FROM Pivoted p \r\n" +
            "ORDER BY p.indigenous_status, p.sex;";

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                String lgacode = results.getString("lga_code");
                String lganame = results.getString("lga_name");
                String indigenous_status = results.getString("indigenous_status");
                int total_2016 = results.getInt("total_2016");
                int total_2021 = results.getInt("total_2021");
                int change = results.getInt("change");
                String sexs = results.getString("sex");
                String status = results.getString("status");

                gapanalysisClass GAP = new gapanalysisClass(lgacode, lganame, sexs, indigenous_status, 
                    total_2016, total_2021, change, status);
                gapAnalysis.add(GAP);
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return gapAnalysis;
    }

    public ArrayList<gapanalysisClass> getEducationData(String lga_code, List<String> statuses, List<String> sexes, List<String> educationLevels) {
        ArrayList<gapanalysisClass> results = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            StringBuilder whereClause = new StringBuilder();
            List<String> conditions = new ArrayList<>();

            if (lga_code != null && !lga_code.trim().isEmpty()) {
                conditions.add("el.lga_code = '" + lga_code + "'");
            }

            if (statuses != null && !statuses.isEmpty()) {
                List<String> mappedStatuses = new ArrayList<>();
                for (String status : statuses) {
                    switch (status.toLowerCase()) {
                        case "indigenous":
                            mappedStatuses.add("'indig'");
                            break;
                        case "non-indigenous":
                            mappedStatuses.add("'non_indig'");
                            break;
                        case "not-stated":
                            mappedStatuses.add("'indig_stat_notstated'");
                            break;
                    }
                }
                if (!mappedStatuses.isEmpty()) {
                    conditions.add("el.indigenous_status IN (" + String.join(",", mappedStatuses) + ")");
                }
            }

            if (sexes != null && !sexes.isEmpty()) {
                List<String> mappedSexes = new ArrayList<>();
                for (String sex : sexes) {
                    switch (sex.toLowerCase()) {
                        case "male":
                            mappedSexes.add("'m'");
                            break;
                        case "female":
                            mappedSexes.add("'f'");
                            break;
                    }
                }
                if (!mappedSexes.isEmpty()) {
                    conditions.add("el.sex IN (" + String.join(",", mappedSexes) + ")");
                }
            }

            if (educationLevels != null && !educationLevels.isEmpty()) {
                conditions.add("el.Category IN ('" + String.join("','", educationLevels) + "')");
            }

            if (!conditions.isEmpty()) {
                whereClause.append("WHERE ").append(String.join(" AND ", conditions));
            }

            String query = 
                "WITH EducationData AS (\r\n" +
                "    SELECT \r\n" +
                "        l.name AS lga_name,\r\n" +
                "        el.lga_code,\r\n" +
                "        CASE \r\n" +
                "            WHEN el.sex = 'm' THEN 'Male' \r\n" +
                "            WHEN el.sex = 'f' THEN 'Female' \r\n" +
                "            ELSE 'Unknown' \r\n" +
                "        END AS sex,\r\n" +
                "        CASE \r\n" +
                "            WHEN el.indigenous_status = 'indig' THEN 'Indigenous'\r\n" +
                "            WHEN el.indigenous_status = 'non_indig' THEN 'Non-Indigenous'\r\n" +
                "            WHEN el.indigenous_status = 'indig_stat_notstated' THEN 'Not Stated'\r\n" +
                "            ELSE 'Unknown'\r\n" +
                "        END AS indigenous_status,\r\n" +
                "        CASE \r\n" +
                "            WHEN el.Category = 'pd_gd_gc' THEN 'Postgraduate Degree Level'\r\n" +
                "            WHEN el.Category = 'bd' THEN 'Bachelor Degree Level'\r\n" +
                "            WHEN el.Category = 'adip_dip' THEN 'Advanced Diploma and Diploma Level'\r\n" +
                "            WHEN el.Category = 'ct_iii_iv' THEN 'Certificate III & IV Level'\r\n" +
                "            WHEN el.Category = 'ct_i_ii' THEN 'Certificate I & II Level'\r\n" +
                "            ELSE el.Category\r\n" +
                "        END AS Category,\r\n" +
                "        SUM(CASE WHEN el.lga_year = 2016 THEN el.count ELSE 0 END) AS total_2016,\r\n" +
                "        SUM(CASE WHEN el.lga_year = 2021 THEN el.count ELSE 0 END) AS total_2021\r\n" +
                "    FROM Education_level el\r\n" +
                "    JOIN lga l ON el.lga_code = l.code AND el.lga_year = l.year\r\n" +
                "    " + whereClause.toString() + "\r\n" +
                "    GROUP BY l.name, el.lga_code, el.sex, el.indigenous_status, el.Category\r\n" +
                ")\r\n" +
                "SELECT \r\n" +
                "    lga_name,\r\n" +
                "    lga_code,\r\n" +
                "    sex,\r\n" +
                "    indigenous_status,\r\n" +
                "    Category,\r\n" +
                "    total_2016,\r\n" +
                "    total_2021,\r\n" +
                "    total_2021 - total_2016 AS change,\r\n" +
                "    CASE \r\n" +
                "        WHEN total_2021 - total_2016 > 0 THEN 'Increased'\r\n" +
                "        WHEN total_2021 - total_2016 < 0 THEN 'Decreased'\r\n" +
                "        ELSE 'No Change'\r\n" +
                "    END AS status\r\n" +
                "FROM EducationData\r\n" +
                "ORDER BY lga_name, indigenous_status, sex, Category;";

            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                gapanalysisClass data = new gapanalysisClass(
                    rs.getString("lga_code"),
                    rs.getString("lga_name"),
                    rs.getString("sex"),
                    rs.getString("indigenous_status"),
                    rs.getInt("total_2016"),
                    rs.getInt("total_2021"),
                    rs.getInt("change"),
                    rs.getString("status")
                );
                data.setCategory(rs.getString("Category"));
                results.add(data);
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return results;
    }

    public ArrayList<gapanalysisClass> getHealthData(String lga_code, List<String> statuses, List<String> sexes, List<String> conditions) {
        ArrayList<gapanalysisClass> results = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            StringBuilder whereClause = new StringBuilder();
            List<String> whereConditions = new ArrayList<>();

            if (lga_code != null && !lga_code.trim().isEmpty()) {
                whereConditions.add("h.lga_code = '" + lga_code + "'");
            }

            if (statuses != null && !statuses.isEmpty()) {
                List<String> mappedStatuses = new ArrayList<>();
                for (String status : statuses) {
                    switch (status.toLowerCase()) {
                        case "indigenous":
                            mappedStatuses.add("'indig'");
                            break;
                        case "non-indigenous":
                            mappedStatuses.add("'non_indig'");
                            break;
                        case "not-stated":
                            mappedStatuses.add("'indig_stat_notstated'");
                            break;
                    }
                }
                if (!mappedStatuses.isEmpty()) {
                    whereConditions.add("h.indigenous_status IN (" + String.join(",", mappedStatuses) + ")");
                }
            }

            if (sexes != null && !sexes.isEmpty()) {
                List<String> mappedSexes = new ArrayList<>();
                for (String sex : sexes) {
                    switch (sex.toLowerCase()) {
                        case "male":
                            mappedSexes.add("'m'");
                            break;
                        case "female":
                            mappedSexes.add("'f'");
                            break;
                    }
                }
                if (!mappedSexes.isEmpty()) {
                    whereConditions.add("h.sex IN (" + String.join(",", mappedSexes) + ")");
                }
            }

            if (conditions != null && !conditions.isEmpty()) {
                whereConditions.add("h.Category IN ('" + String.join("','", conditions) + "')");
            }

            // Add condition for 2021 data only
            whereConditions.add("h.lga_year = 2021");

            if (!whereConditions.isEmpty()) {
                whereClause.append("WHERE ").append(String.join(" AND ", whereConditions));
            }

            String query = 
            // Because the data of health is only avaiable in 2021, therefore we set the total_2016 data is 0 and the status is current 
                "SELECT \r\n" +
                "    l.name AS lga_name,\r\n" +
                "    h.lga_code,\r\n" +
                "    CASE \r\n" +
                "        WHEN h.sex = 'm' THEN 'Male' \r\n" +
                "        WHEN h.sex = 'f' THEN 'Female' \r\n" +
                "        ELSE 'Unknown' \r\n" +
                "    END AS sex,\r\n" +
                "    CASE \r\n" +
                "        WHEN h.indigenous_status = 'indig' THEN 'Indigenous'\r\n" +
                "        WHEN h.indigenous_status = 'non_indig' THEN 'Non-Indigenous'\r\n" +
                "        WHEN h.indigenous_status = 'indig_stat_notstated' THEN 'Not Stated'\r\n" +
                "        ELSE 'Unknown'\r\n" +
                "    END AS indigenous_status,\r\n" +
                "    CASE \r\n" +
                "        WHEN h.Category = 'arthritis' THEN 'Arthritis'\r\n" +
                "        WHEN h.Category = 'asthma' THEN 'Asthma'\r\n" +
                "        WHEN h.Category = 'cancer' THEN 'Cancer (any type) including remission'\r\n" +
                "        WHEN h.Category = 'dementia' THEN 'Dementia (any stage) including Alzheimers'\r\n" +
                "        WHEN h.Category = 'diabetes' THEN 'Diabetes (excluding Gestational)'\r\n" +
                "        WHEN h.Category = 'heartdisease' THEN 'Heart Disease (including Heart Attack or Angina)'\r\n" +
                "        WHEN h.Category = 'kidneydisease' THEN 'Kidney Disease'\r\n" +
                "        WHEN h.Category = 'lungcondition' THEN 'Lung Condition (including COPD or Emphysema)'\r\n" +
                "        WHEN h.Category = 'mentalhealth' THEN 'Mental Health Condition (including depression or anxiety)'\r\n" +
                "        WHEN h.Category = 'stroke' THEN 'Stroke (with ongoing long-term health impacts)'\r\n" +
                "        WHEN h.Category = 'other' THEN 'Other Long-Term Health Condition'\r\n" +
                "        ELSE h.Category\r\n" +
                "    END AS Category,\r\n" +
                "    h.count AS total_2021,\r\n" +
                "    0 AS total_2016,\r\n" +
                "    h.count AS change,\r\n" +
                "    'Current' AS status\r\n" +
                "FROM Health h\r\n" +
                "JOIN lga l ON h.lga_code = l.code AND h.lga_year = l.year\r\n" +
                whereClause.toString() + "\r\n" +
                "ORDER BY l.name, indigenous_status, sex, Category;";

            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                gapanalysisClass data = new gapanalysisClass(
                    rs.getString("lga_code"),
                    rs.getString("lga_name"),
                    rs.getString("sex"),
                    rs.getString("indigenous_status"),
                    rs.getInt("total_2016"),
                    rs.getInt("total_2021"),
                    rs.getInt("change"),
                    rs.getString("status")
                );
                data.setCategory(rs.getString("Category"));
                results.add(data);
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return results;
    }

    public ArrayList<gapanalysisClass> getSchoolCompletionData(String lga_code, List<String> statuses, List<String> sexes, List<String> completions) {
        ArrayList<gapanalysisClass> results = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            StringBuilder whereClause = new StringBuilder();
            List<String> conditions = new ArrayList<>();

            if (lga_code != null && !lga_code.trim().isEmpty()) {
                conditions.add("sc.lga_code = '" + lga_code + "'");
            }

            if (statuses != null && !statuses.isEmpty()) {
                List<String> mappedStatuses = new ArrayList<>();
                for (String status : statuses) {
                    switch (status.toLowerCase()) {
                        case "indigenous":
                            mappedStatuses.add("'indig'");
                            break;
                        case "non-indigenous":
                            mappedStatuses.add("'non_indig'");
                            break;
                        case "not-stated":
                            mappedStatuses.add("'indig_stat_notstated'");
                            break;
                    }
                }
                if (!mappedStatuses.isEmpty()) {
                    conditions.add("sc.indigenous_status IN (" + String.join(",", mappedStatuses) + ")");
                }
            }

            if (sexes != null && !sexes.isEmpty()) {
                List<String> mappedSexes = new ArrayList<>();
                for (String sex : sexes) {
                    switch (sex.toLowerCase()) {
                        case "male":
                            mappedSexes.add("'m'");
                            break;
                        case "female":
                            mappedSexes.add("'f'");
                            break;
                    }
                }
                if (!mappedSexes.isEmpty()) {
                    conditions.add("sc.sex IN (" + String.join(",", mappedSexes) + ")");
                }
            }

            if (completions != null && !completions.isEmpty()) {
                conditions.add("sc.Category IN ('" + String.join("','", completions) + "')");
            }

            if (!conditions.isEmpty()) {
                whereClause.append("WHERE ").append(String.join(" AND ", conditions));
            }

            String query = 
                "WITH SchoolCompletionData AS (\r\n" +
                "    SELECT \r\n" +
                "        l.name AS lga_name,\r\n" +
                "        sc.lga_code,\r\n" +
                "        CASE \r\n" +
                "            WHEN sc.sex = 'm' THEN 'Male' \r\n" +
                "            WHEN sc.sex = 'f' THEN 'Female' \r\n" +
                "            ELSE 'Unknown' \r\n" +
                "        END AS sex,\r\n" +
                "        CASE \r\n" +
                "            WHEN sc.indigenous_status = 'indig' THEN 'Indigenous'\r\n" +
                "            WHEN sc.indigenous_status = 'non_indig' THEN 'Non-Indigenous'\r\n" +
                "            WHEN sc.indigenous_status = 'indig_stat_notstated' THEN 'Not Stated'\r\n" +
                "            ELSE 'Unknown'\r\n" +
                "        END AS indigenous_status,\r\n" +
                "        CASE \r\n" +
                "            WHEN sc.Category = 'did_not_go_to_school' THEN 'Did not attend School'\r\n" +
                "            WHEN sc.Category = 'y8_below' THEN 'Year 8 or Below'\r\n" +
                "            WHEN sc.Category = 'y9_equivalent' THEN 'Year 9 or Equivalent'\r\n" +
                "            WHEN sc.Category = 'y10_equivalent' THEN 'Year 10 or Equivalent'\r\n" +
                "            WHEN sc.Category = 'y11_equivalent' THEN 'Year 11 or Equivalent'\r\n" +
                "            WHEN sc.Category = 'y12_equivalent' THEN 'Year 12 or Equivalent'\r\n" +
                "            ELSE sc.Category\r\n" +
                "        END AS Category,\r\n" +
                "        SUM(CASE WHEN sc.lga_year = 2016 THEN sc.count ELSE 0 END) AS total_2016,\r\n" +
                "        SUM(CASE WHEN sc.lga_year = 2021 THEN sc.count ELSE 0 END) AS total_2021\r\n" +
                "    FROM School_completion sc\r\n" +
                "    JOIN lga l ON sc.lga_code = l.code AND sc.lga_year = l.year\r\n" +
                "    " + whereClause.toString() + "\r\n" +
                "    GROUP BY l.name, sc.lga_code, sc.sex, sc.indigenous_status, sc.Category\r\n" +
                ")\r\n" +
                "SELECT \r\n" +
                "    lga_name,\r\n" +
                "    lga_code,\r\n" +
                "    sex,\r\n" +
                "    indigenous_status,\r\n" +
                "    Category,\r\n" +
                "    total_2016,\r\n" +
                "    total_2021,\r\n" +
                "    total_2021 - total_2016 AS change,\r\n" +
                "    CASE \r\n" +
                "        WHEN total_2021 - total_2016 > 0 THEN 'Increased'\r\n" +
                "        WHEN total_2021 - total_2016 < 0 THEN 'Decreased'\r\n" +
                "        ELSE 'No Change'\r\n" +
                "    END AS status\r\n" +
                "FROM SchoolCompletionData\r\n" +
                "ORDER BY lga_name, indigenous_status, sex, Category;";

            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                gapanalysisClass data = new gapanalysisClass(
                    rs.getString("lga_code"),
                    rs.getString("lga_name"),
                    rs.getString("sex"),
                    rs.getString("indigenous_status"),
                    rs.getInt("total_2016"),
                    rs.getInt("total_2021"),
                    rs.getInt("change"),
                    rs.getString("status")
                );
                data.setCategory(rs.getString("Category"));
                results.add(data);
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return results;
    }
}
  



  

