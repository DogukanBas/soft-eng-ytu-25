-- USERS
CREATE TABLE users (
    personalNo VARCHAR(20) PRIMARY KEY UNIQUE NOT NULL,
    passwordHash TEXT NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    userType VARCHAR(20) CHECK (userType IN ('team_member', 'manager', 'accountant', 'admin')) NOT NULL
);


-- EMPLOYEES
CREATE TABLE employee (
    personalNo VARCHAR(20) PRIMARY KEY REFERENCES users(personalNo) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    deptId INTEGER NOT NULL
);

-- DEPARTMENTS
CREATE TABLE departments (
    deptId SERIAL PRIMARY KEY,
    deptname VARCHAR(100) NOT NULL,
    remainingBudget NUMERIC(12, 2) DEFAULT 0,
    initialBudget NUMERIC(12, 2) DEFAULT 0,
    deptManager VARCHAR(20) REFERENCES users(personalNo)
);

-- TICKETS
CREATE TABLE tickets (
    ticketId SERIAL PRIMARY KEY,
    employeeId VARCHAR(20) NOT NULL REFERENCES users(personalNo) ON DELETE CASCADE,
    managerId VARCHAR(20) NOT NULL REFERENCES users(personalNo),
    costType VARCHAR(50) NOT NULL,
    amount NUMERIC(10, 2) NOT NULL CHECK (amount >= 0)
);

-- APPROVE HISTORY
CREATE TABLE approveHistory (
    id SERIAL PRIMARY KEY,
    ticketId INTEGER NOT NULL REFERENCES tickets(ticketId) ON DELETE CASCADE,
    date DATE NOT NULL DEFAULT CURRENT_DATE,
    status TEXT NOT NULL CHECK (
        status IN (
            'SENT_TO_MANAGER',
	        'REJECTED_BY_MANAGER_CAN_BE_FIXED',
	        'CLOSED_AS_REJECTED_BY_MANAGER',
	        'SENT_TO_ACCOUNTANT',
	        'CLOSED_AS_APPROVED',
	        'REJECTED_BY_ACCOUNTANT_CAN_BE_FIXED',
	        'CLOSED_AS_REJECTED_BY_ACCOUNTANT',
	        'CANCELED_BY_USER'
        )
    ),
    description TEXT,
    actorId VARCHAR(20) REFERENCES users(personalNo)
);

-- BUDGET BY COST TYPE
CREATE TABLE budgetByCostType (
    id SERIAL PRIMARY KEY,
    typeName VARCHAR(100) UNIQUE NOT NULL,
    remainingBudget NUMERIC(12, 2) DEFAULT 0,
    initialBudget NUMERIC(12, 2) DEFAULT 0,
	maxCost NUMERIC(12, 2) DEFAULT 0
);

-- ATTACHMENTS
CREATE TABLE attachments (
    id SERIAL PRIMARY KEY,
    ticketId INTEGER NOT NULL REFERENCES tickets(ticketId) ON DELETE CASCADE,
    invoice oid NOT NULL
);


INSERT INTO users (personalNo,passwordHash,email,userType) VALUES('25200402','$2a$10$yhfUwYUG1ustinefmnVCA.vD.yCMdahFEIXe52Dj2r5yzkiYi1oGm','admin@costms.com','admin');