# Find a Quote
The purpose of this application is to find a quote from a market of lenders for 36-month loans that apply interest on a monthly basis

##Get Started
After cloning the project, run maven build for creating the required .jar
```mvn clean verify```

The application takes the load amount as command line parameters:
```./zopa-rate /loan_amount```


The application takes the load amount as command line parameters. You can start using the application with the following command:
```./zopa-rate <loan_amount>```

```./zopa-rate 1000```

Loan amount should be between any £100 increment between £1000 and £15000 inclusive. If you pass a loan amount outside the range, you'll see the following message:
```It is not possible to provide a quote.```

If the application can find a quote for the requested loan amount, the message will contain the details of the quoute in the following format:

```
Requested amount: £XXXX 
Annual Interest Rate: X.X%
Monthly repayment: £XXXX.XX
Total repayment: £XXXX.XX
```

If the market does not have enough offers to fulfil the request, you'll see the following message:
```It is not possible to provide a quote.```


##Code structure
**com.stan.zopa.market.LenderService** <br/>
   This service gets the lenders details in the market from market.csv and returns the lenders list when it's requested. 
  
**com.stan.zopa.quote.QuoteService** <br/>
   This service finds the quote by the requested loan amounts.
   
**com.stan.zopa.P2OQuoteService** <br/>
    The result prints in Main class after getting from QuoteService.
