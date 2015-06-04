scope({c0_Currency:0, c0_Mortgage:0, c0_MortgageTerm:2, c0_Percentage:0, c0_amortization:0, c0_balance:0, c0_biweekly:0, c0_closed:0, c0_conventional:0, c0_currentInterestRate:0, c0_currentPayment:0, c0_financingAvailable:0, c0_fixedForTheFullTerm:0, c0_insured:0, c0_interestRate:0, c0_kind:0, c0_loanToValue:0, c0_monthly:0, c0_open:0, c0_paymentFrequency:0, c0_principalMortgageAmount:0, c0_propertyValue:0, c0_resetTogetherWithPaymentAmountEachTimeScotiabankPrimeRateChanges:0, c0_semiMonthly:0, c0_term:0, c0_valueProposition:0, c0_weekly:0});
defaultScope(1);
intRange(-8, 7);
stringLength(16);

c0_Currency = Abstract("c0_Currency");
c0_Percentage = Abstract("c0_Percentage");
c0_Mortgage = Abstract("c0_Mortgage");
c0_MortgageTerm = Abstract("c0_MortgageTerm");
c0_valueProposition = c0_Mortgage.addChild("c0_valueProposition").withCard(1, 1);
c0_term = c0_Mortgage.addChild("c0_term").withCard(1, 1);
c0_kind = c0_Mortgage.addChild("c0_kind").withCard(1, 1).withGroupCard(1, 1);
c0_open = c0_kind.addChild("c0_open").withCard(0, 1);
c0_closed = c0_kind.addChild("c0_closed").withCard(0, 1);
c0_principalMortgageAmount = c0_Mortgage.addChild("c0_principalMortgageAmount").withCard(1, 1);
c0_balance = c0_Mortgage.addChild("c0_balance").withCard(1, 1);
c0_propertyValue = c0_Mortgage.addChild("c0_propertyValue").withCard(1, 1);
c0_loanToValue = c0_Mortgage.addChild("c0_loanToValue").withCard(1, 1);
c0_amortization = c0_Mortgage.addChild("c0_amortization").withCard(1, 1);
c0_interestRate = c0_Mortgage.addChild("c0_interestRate").withCard(1, 1).withGroupCard(1, 1);
c0_fixedForTheFullTerm = c0_interestRate.addChild("c0_fixedForTheFullTerm").withCard(0, 1);
c0_resetTogetherWithPaymentAmountEachTimeScotiabankPrimeRateChanges = c0_interestRate.addChild("c0_resetTogetherWithPaymentAmountEachTimeScotiabankPrimeRateChanges").withCard(0, 1);
c0_currentInterestRate = c0_Mortgage.addChild("c0_currentInterestRate").withCard(1, 1);
c0_currentPayment = c0_Mortgage.addChild("c0_currentPayment").withCard(1, 1);
c0_paymentFrequency = c0_Mortgage.addChild("c0_paymentFrequency").withCard(1, 1).withGroupCard(1, 1);
c0_weekly = c0_paymentFrequency.addChild("c0_weekly").withCard(0, 1);
c0_biweekly = c0_paymentFrequency.addChild("c0_biweekly").withCard(0, 1);
c0_semiMonthly = c0_paymentFrequency.addChild("c0_semiMonthly").withCard(0, 1);
c0_monthly = c0_paymentFrequency.addChild("c0_monthly").withCard(0, 1);
c0_financingAvailable = c0_Mortgage.addChild("c0_financingAvailable").withCard(1, 1).withGroupCard(1, 1);
c0_conventional = c0_financingAvailable.addChild("c0_conventional").withCard(0, 1);
c0_insured = c0_financingAvailable.addChild("c0_insured").withCard(0, 1);
c0_sixMonths = Clafer("c0_sixMonths").withCard(1, 1).extending(c0_MortgageTerm);
c0_oneYear = Clafer("c0_oneYear").withCard(1, 1).extending(c0_MortgageTerm);
c0_Currency.refTo(Int);
c0_Percentage.refTo(Int);
c0_valueProposition.refTo(string);
c0_term.refTo(c0_MortgageTerm);
c0_principalMortgageAmount.refTo(c0_Currency);
c0_balance.refTo(c0_Currency);
c0_propertyValue.refTo(c0_Currency);
c0_loanToValue.refTo(c0_Percentage);
c0_amortization.refTo(Int);
c0_currentInterestRate.refTo(c0_Percentage);
c0_currentPayment.refTo(c0_Currency);
c0_Percentage.addConstraint(and(greaterThanEqual(joinRef($this()), constant(0)), lessThanEqual(joinRef($this()), constant(100))));
c0_Mortgage.addConstraint(and(lessThanEqual(constant(5000), joinRef(joinRef(join($this(), c0_principalMortgageAmount)))), lessThanEqual(joinRef(joinRef(join($this(), c0_principalMortgageAmount))), constant(9999999))));
c0_Mortgage.addConstraint(lessThanEqual(joinRef(joinRef(join($this(), c0_balance))), joinRef(joinRef(join($this(), c0_principalMortgageAmount)))));
c0_Mortgage.addConstraint(and(lessThanEqual(constant(1), joinRef(join($this(), c0_amortization))), lessThanEqual(joinRef(join($this(), c0_amortization)), constant(30))));