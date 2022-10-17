# final-project

<h2>Class Diagram</h2>

![Class Diagram](https://user-images.githubusercontent.com/108625085/196240022-c713b314-8a56-44b1-9ac7-1022183c5328.png)

<h2>Use Case Diagram</h2>


<h2>Requirements</h2>


<h3>1. The system must have 4 types of accounts: StudentChecking, Checking, Savings, and CreditCard.</h3>

<h4>Checking</h4>


Checking Accounts should have:

<ul>A balance</ul>
<ul>A secretKey</ul>
<ul>A PrimaryOwner</ul>
<ul>An optional SecondaryOwner</ul>
<ul>A minimumBalance</ul>
<ul>A penaltyFee</ul>
<ul>A monthlyMaintenanceFee</ul>
<ul>A creationDate</ul>
<ul>A status (FROZEN, ACTIVE)</ul>

<h4>StudentChecking</h4>


Student Checking Accounts are identical to Checking Accounts except that they do NOT have:

<ul>A monthlyMaintenanceFee</ul>
<ul>A minimumBalance</ul>

<h4>Savings</h4>


Savings are identical to Checking accounts except that they

<ul>Do NOT have a monthlyMaintenanceFee</ul>
<ul>Do have an interestRate</ul>

<h4>Credit Card</h4>


CreditCard Accounts have:

<ul>A balance</ul>
<ul>A PrimaryOwner</ul>
<ul>An optional SecondaryOwner</ul>
<ul>A creditLimit</ul>
<ul>An interestRate</ul>
<ul>A penaltyFee</ul>

<h3>2. The system must have 3 types of Users: Admins, AccountHolders and Third Party Users.</h3>

<h4>AccountHolders</h4>


The AccountHolders should be able to access their own accounts and only their accounts when passing the correct credentials using Basic Auth. 

AccountHolders have:

<ul>A name</ul>
<ul>Date of birth</ul>
<ul>A primaryAddress (which should be a separate address class)</ul>
<ul>An optional mailingAddress</ul>

<h4>Admins</h4>


<ul>Admins only have a name</ul>


<h4>ThirdParty</h4>


<ul>The ThirdParty Accounts have a hashed key and a name.</ul>


<h3>3. Admins can create new accounts. When creating a new account they can create Checking, Savings, or CreditCard Accounts.</h3>

<h4>Savings</h4>


<ul>Savings accounts have a default interest rate of 0.0025</ul>
<ul>Savings accounts may be instantiated with an interest rate other than the default, with a maximum interest rate of 0.5</ul>
<ul>Savings accounts should have a default minimumBalance of 1000</ul>
<ul>Savings accounts may be instantiated with a minimum balance of less than 1000 but no lower than 100</ul>

<h4>CreditCards</h4>


<ul>CreditCard accounts have a default creditLimit of 100</ul>
<ul>CreditCards may be instantiated with a creditLimit higher than 100 but not higher than 100000</ul>
<ul>CreditCards have a default interestRate of 0.2</ul>
<ul>CreditCards may be instantiated with an interestRate less than 0.2 but not lower than 0.1</ul>

<h4>CheckingAccounts</h4>


<ul>When creating a new Checking account, if the primaryOwner is less than 24, a StudentChecking account should be created otherwise a regular Checking Account should be created.</ul>
<ul>Checking accounts should have a minimumBalance of 250 and a monthlyMaintenanceFee of 12</ul>

<ul>Interest and Fees should be applied appropriately</ul>

<h4>PenaltyFee</h4>


<ul>The penaltyFee for all accounts should be 40.</ul>
<ul>If any account drops below the minimumBalance, the penaltyFee should be deducted from the balance automatically</ul>

<h4>InterestRate</h4>


<ul>Interest on savings accounts is added to the account annually at the rate of specified interestRate per year. That means that if I have 1000000 in a savings account with a 0.01 interest rate, 1% of 1 Million is added to my account after 1 year. When a savings account balance is accessed, you must determine if it has been 1 year or more since either the account was created or since interest was added to the account, and add the appropriate interest to the balance if necessary.</ul>

<ul>Interest on credit cards is added to the balance monthly. If you have a 12% interest rate (0.12) then 1% interest will be added to the account monthly. When the balance of a credit card is accessed, check to determine if it has been 1 month or more since the account was created or since interested was added, and if so, add the appropriate interest to the balance.</ul>


<h3>4. Account Access</h3>

<h4>Admins</h4>


<ul>Admins should be able to access the balance for any account and to modify it.</ul>

<h4>AccountHolders</h4>


<ul>AccountHolders should be able to access their own account balance</ul>
<ul>Account holders should be able to transfer money from any of their accounts to any other account (regardless of owner). The transfer should only be processed if the account has sufficient funds. The user must provide the Primary or Secondary owner name and the id of the account that should receive the transfer.</ul>

<h4>Third-Party Users</h4>


<ul>There must be a way for third-party users to receive and send money to other accounts.</ul>
<ul>Third-party users must be added to the database by an admin.</ul>
<ul>In order to receive and send money, Third-Party Users must provide their hashed key in the header of the HTTP request. They also must provide the amount, the Account id and the account secret key.</ul>

<h2>Technical Requirements</h2>
<ul>Include a Java/Spring Boot backend.</ul>
<ul>Everything should be stored in MySQL database tables.</ul>
<ul>Include at least 1 GET, POST, PUT/PATCH, and DELETE route.</ul>
<ul>Include authentication with Spring Security.</ul>
<ul>Include unit and integration tests.</ul>
<ul>Include robust error handling.</ul>
<ul>You must use the Money class for all currency and BigDecimal for any other decimal or large number math.</ul>
