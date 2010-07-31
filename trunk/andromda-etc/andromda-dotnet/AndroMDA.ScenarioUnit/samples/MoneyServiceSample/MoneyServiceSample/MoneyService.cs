using System;

namespace MoneyServiceSample
{
    public class MoneyService
    {
        public static Money Add(Money m1, Money m2)
        {
            Money result = new Money();
            result.currency = m1.currency;
            if (m1.currency == m2.currency)
            {
                result.amount = m1.amount + m2.amount;
            }
            else
            {
                //add a conversion factor to convert from m2.currency to m1.currency.
                //A constant is used here. In real life, this will be some complicated logic.
                result.amount = m1.amount + m2.amount + 20;
            }
            return result;
        }
    }
}
