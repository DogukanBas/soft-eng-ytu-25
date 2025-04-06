# Cost Management System 
## Use Case Scenarios
- [Create Ticket](#create-ticket)
- [Edit Budget](#edit-budget)
- [Generate Report](#generate-report)
- [Create User](#create-user)
- [Create Department](#create-department)
- [View Ticket](#view-ticket)

## Create Ticket
### Introduction
- **Primary Actor**: Team Member  
- **Stakeholders and Interests**:  
  - Team Member: Wants to create an expense request  
  - Manager: Wants to quickly review and approve the created request.  
  - Accountant: Wants to validate the invoice and expense item of the request approved by the Manager, close the ticket, and process the financial transactions.  
- **Preconditions**: The Team Member must be logged into the system.  
- **Postconditions**: The ticket is saved into the system and closed, the expense is deducted from the budget.  

### Main Scenario
1. The Team Member selects the "Create Ticket" option in the system.
2. The Team Member enters the expense item and invoice details.
3. The Team Member enters the amount of the expense.
4. The request is sent to the manager for approval.
5. The Manager approves the request.
6. The Team Member sees that the request has been approved by the manager.
7. The Accountant receives the request approved by the Manager and verifies the expense item and invoice details.
8. After verifying the expense item and invoice details, the Accountant closes the ticket.
9. The ticket is closed, and the expense item and invoice details are saved into the system.
10. The expense is deducted from the budget.

### Alternative Scenarios
1-6a. **Ticket created by the Manager**  
  1. The Manager creates the ticket.  
  2. The Manager directly sends it to the Accountant without needing an additional manager approval.

2-7a. **Missing Invoice Information**  
  1. The Team Member wants to complete the missing invoice details for a previously created ticket that has not yet been sent to the Accountant for approval.
 
5a. **Manager requests the ticket to be edited.**  
   1. The Manager sends the ticket back to the Team Member with an explanation to correct any missing or incorrect information.  
   2. The Team Member corrects the ticket based on the Manager’s feedback.

5b. **Manager rejects and closes the ticket.**  
  1. The Manager rejects the request and closes the ticket.  
  2. The Team Member can view the ticket rejected by the Manager.

8a. **Accountant edits the expense item of the ticket.**  
  1. The Accountant corrects the incorrect expense item.

8b. **Accountant requests the ticket to be edited.**  
  1. The Accountant sends the ticket back to the Team Member with an explanation to correct any missing or incorrect information.  
  2. The Team Member corrects the ticket based on the Accountant’s feedback.

8c. **Accountant does not verify the expense item and invoice details.**  
  1. The Accountant does not verify the expense item and invoice details.  
  2. The Accountant rejects and closes the ticket.  
  3. The Team Member can view the ticket rejected by the Accountant.

---

## Edit Budget
### Introduction
- **Primary Actor**: Accountant  
- **Stakeholders and Interests**:  
  - Accountant: Wants to adjust the budget according to previous expenses.
- **Preconditions**: The Accountant must be logged into the system, and the department whose budget will be edited must have been added to the system by the admin.
- **Postconditions**: The budget is adjusted, and the cost items are saved into the system.

### Main Scenario
1. The Accountant selects the "Edit Budget" option in the system.
2. The Accountant adjusts the budget for the cost items and departments.
3. The Accountant saves the adjusted budget.

### Alternative Scenario
1-2a. **New Cost Item**  
  1. The Accountant creates a new cost item.  
  2. The Accountant saves the budget created for the new cost item.

---

## Generate Report
### Introduction
- **Primary Actor**: Accountant  
- **Stakeholders and Interests**:  
  - Accountant: Wants to generate reports related to the expenses of cost items, individuals, and departments from previous periods.
- **Preconditions**: The Accountant must be logged into the system. If the report will be for an individual or department, they must have been added to the system by the admin. If the report will be for a cost item, it must have been added to the system by the accountant.
- **Postconditions**: Graphs and reports are generated, and recommendations for the next period's budget are provided.

### Main Scenario
1. The Accountant goes to the report section in the system.
2. The Accountant selects the date range and the type for the report (employee/department/cost item).
3. The Accountant views the recommendations for the upcoming budgets.

### Alternative Scenario
2-3a. **No Expense Found**  
  If there are no expenses within the given date range, the system returns an error and does not generate the report.


---

## Create User
### Introduction
- **Primary Actor**: Admin  
- **Stakeholders and Interests**:  
  - Admin: Wants to create the employee's profile with minimal effort.  
  - Employee: Wants their account to be created quickly and delivered to them.
- **Preconditions**: The Admin account must be defined in the system.  
- **Postconditions**: A new user is created.

### Main Scenario
1. The "Create User" option is selected.  
2. The user information is entered.  
3. The user is created.

### Alternative Scenarios
2-3a. **Email Conflict**  
  An error occurs, and the user is not created.

2a. **Adding an Accountant**  
  The manager/department information is not required.

--

## Create Department
### Introduction
- **Primary Actor**: Admin  
- **Stakeholders and Interests**:  
  - Admin: Wants to create the department quickly in the system.
- **Postconditions**: The department is created, and a notification is sent to the accountant to create the budget.

### Main Scenario
1. The Admin clicks the "Create Department" button.  
2. The Admin enters the department name.  
3. The department is created.

### Alternative Scenario
2-3a. **Department Conflict**  
  An error occurs, and the department is not created.


--

## View Ticket
### Introduction
- **Primary Actor**: Team Member  
- **Stakeholders and Interests**:  
  - Team Member: Wants to view their previous tickets and details.  
  - Manager: Wants to view the tickets they have approved in the past.  
  - Accountant: Wants to view the tickets they have approved in the past.
- **Preconditions**: The employee must be logged into the system and must have created/approved tickets in the past.
- **Postconditions**: Past tickets are displayed.

### Main Scenario
1. The Team Member clicks the "Ticket History" button.  
2. Past tickets are listed.
3. The Team Member clicks on the ticket to view the details.

### Alternative Scenarios
1-2a. **Other Users**  
  The Accountant/Manager views the tickets they have approved.

2a. **Edit**  
   If the ticket has not yet been approved by the manager for the Team Member, or if the ticket has not yet been approved by the accountant for the Manager, it can be edited.
