Scenario: Aggregation of enrollment fees per student
Given a fee payment file is received with following payment records
| studentId | subjectId | month | feesPaid |
| student-001 | subject-001 | Mar-2023 | 500 |
| student-002 | subject-001 | Mar-2023 | 500 |
| student-002 | subject-001 | Apr-2023 | 500 |
| student-001 | subject-002 | Mar-2023 | 550 |
When the report is generated:
The output file should contain following records
| studentId   | month    | feesCollected |
| student-001 | Mar-2023 | 1050 |
| student-002 | Mar-2023 | 500  |
| student-002 | Apr-2023 | 500  |