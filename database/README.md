# Cost Management System – Database Schema

This represents the database architecture.
## Users

```sql
users(userId, personalNo, passwordHash, userType)
```

Stores all system users' basic authentication data.

- `userId`: Unique user identifier  
- `personalNo`: Internal employee ID  
- `passwordHash`: Hashed user password
- `email`: E-mail 
- `userType`: User role (`team-member`, `manager`, `accountant`, `admin`)

## Employees

```sql
employee(userId, personalNo, name, surname, deptId)
```

Holds detailed information about users who are employees.

- `userId`: Foreign key referencing `users`  
- `personalNo`: Employee ID  
- `name`, `surname`: First and last name  
- `deptId`: Department ID the employee belongs to

## Departments

```sql
departments(deptId, deptname, remainingBudget, initialBudget, deptManager)
```

Represents organizational departments and their budgets.

- `deptname`: Name of the department
- `remainingBudget`: Current remaining budget  
- `initialBudget`: Budget at the beginning of the cycle
- `deptManager`: DepartmentManagerPersonalNo

## Tickets

```sql
tickets(ticketid, employeeid, managerid, costType, amount)
```

Stores all expense requests created by employees.

- `employeeid`: ID of the employee who created the ticket  
- `managerid`: Manager responsible for approval  
- `costType`: Type of expense (e.g., travel, meal)  
- `amount`: Requested amount
## Approve History

```sql
approveHistory(approveId, ticketid, actorId, date, status, desc)
```

Logs each status change of a ticket over time.


### `status` Options

- `sent to manager`  
- `rejected by manager - can be fixed`  
- `closed as rejected by manager`  
- `approved by manager - waiting for invoice`  
- `sent to accountant`  
- `closed as approved`  
- `rejected by accountant - can be fixed`  
- `closed as rejected by accountant`  
- `canceled by user`


## Budget By Cost Type

```sql
budgetByCosttype(id, typeName, remainingBudget, initialBudget)
```

Tracks budget allocation and usage per cost type.

## Attachments

```sql
attachments(ticketid, invoice)
```

Stores uploaded invoice documents linked to tickets.

## Notifications

```sql
notifications(id, type, message, targetId, createdAt)
```

Stores system notifications for users.

- `id`: Unique notification identifier
- `type`: Type of notification (`ALL`, `DEPARTMENT`, `EMPLOYEE`)
- `message`: Notification message content
- `targetId`: Target identifier (null for ALL, departmentId for DEPARTMENT, personalNo for EMPLOYEE)
- `createdAt`: Creation timestamp

---

## Workflow Overview

1. An **team-member** creates an expense ticket.  
2. The ticket is sent to the manager as `sent to manager`.  
3. The **manager** can:
   - Approve it → `approved by manager - waiting for invoice`  
   - Reject it → `rejected by manager - can be fixed` or `closed as rejected by manager`  
4. After invoice is uploaded → status becomes `sent to accountant`  
5. The **accountant** can:
   - Approve → `closed as approved`  
   - Reject → `rejected by accountant - can be fixed` or `closed as rejected by accountant`

---
2-5a: **User** can cancel the ticket at any given time.
