Scenario: Transaction aggregation per customer
Given a transaction file is received with following transaction records
| transactionId | customerId | itemId | quantity | price |
| tx-001 | cust-001 | i-001 | 23 | 23.50 |
| tx-002 | cust-001 | i-002 | 15 | 13.00 |
| tx-003 | cust-002 | i-001 | 3 | 23.50 |
When the report is generated:
The output file should contain following records
| customerId | orderValue |
| cust-001 | 735.50 |
| cust-001 |  70.50 |