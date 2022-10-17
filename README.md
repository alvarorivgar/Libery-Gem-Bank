# final-project

<h2>Requirements</h2>


1. The system must have 4 types of accounts: StudentChecking, Checking, Savings, and CreditCard.

<h3>Checking</h3>


<b>Checking Accounts should have:</b>

<ul>A balance</ul>
A secretKey
A PrimaryOwner
An optional SecondaryOwner
A minimumBalance
A penaltyFee
A monthlyMaintenanceFee
A creationDate
A status (FROZEN, ACTIVE)

<h3>StudentChecking</h3>


<b>Student Checking Accounts are identical to Checking Accounts except that they do NOT have:</b>

A monthlyMaintenanceFee
A minimumBalance

<h3>Savings</h3>


Savings are identical to Checking accounts except that they

Do NOT have a monthlyMaintenanceFee
Do have an interestRate

CreditCard


CreditCard Accounts have:

A balance
A PrimaryOwner
An optional SecondaryOwner
A creditLimit
An interestRate
A penaltyFee

The system must have 3 types of Users: Admins and AccountHolders.

AccountHolders


The AccountHolders should be able to access their own accounts and only their accounts when passing the correct credentials using Basic Auth. AccountHolders have:

A name
Date of birth
A primaryAddress (which should be a separate address class)
An optional mailingAddress

Admins


Admins only have a name


ThirdParty


The ThirdParty Accounts have a hashed key and a name.


Admins can create new accounts. When creating a new account they can create Checking, Savings, or CreditCard Accounts.

Savings


Savings accounts have a default interest rate of 0.0025
Savings accounts may be instantiated with an interest rate other than the default, with a maximum interest rate of 0.5
Savings accounts should have a default minimumBalance of 1000
Savings accounts may be instantiated with a minimum balance of less than 1000 but no lower than 100

CreditCards


CreditCard accounts have a default creditLimit of 100
CreditCards may be instantiated with a creditLimit higher than 100 but not higher than 100000
CreditCards have a default interestRate of 0.2
CreditCards may be instantiated with an interestRate less than 0.2 but not lower than 0.1

CheckingAccounts


When creating a new Checking account, if the primaryOwner is less than 24, a StudentChecking account should be created otherwise a regular Checking Account should be created.
Checking accounts should have a minimumBalance of 250 and a monthlyMaintenanceFee of 12

Interest and Fees should be applied appropriately

PenaltyFee


The penaltyFee for all accounts should be 40.
If any account drops below the minimumBalance, the penaltyFee should be deducted from the balance automatically

InterestRate


Interest on savings accounts is added to the account annually at the rate of specified interestRate per year. That means that if I have 1000000 in a savings account with a 0.01 interest rate, 1% of 1 Million is added to my account after 1 year. When a savings account balance is accessed, you must determine if it has been 1 year or more since either the account was created or since interest was added to the account, and add the appropriate interest to the balance if necessary.

Interest on credit cards is added to the balance monthly. If you have a 12% interest rate (0.12) then 1% interest will be added to the account monthly. When the balance of a credit card is accessed, check to determine if it has been 1 month or more since the account was created or since interested was added, and if so, add the appropriate interest to the balance.


Account Access

Admins


Admins should be able to access the balance for any account and to modify it.

AccountHolders


AccountHolders should be able to access their own account balance
Account holders should be able to transfer money from any of their accounts to any other account (regardless of owner). The transfer should only be processed if the account has sufficient funds. The user must provide the Primary or Secondary owner name and the id of the account that should receive the transfer.

Third-Party Users


There must be a way for third-party users to receive and send money to other accounts.
Third-party users must be added to the database by an admin.
In order to receive and send money, Third-Party Users must provide their hashed key in the header of the HTTP request. They also must provide the amount, the Account id and the account secret key.

<h2>Class Diagram</h2>

![Class Diagram](https://user-images.githubusercontent.com/108625085/196240022-c713b314-8a56-44b1-9ac7-1022183c5328.png)

<h2>Use Case Diagram</h2>
