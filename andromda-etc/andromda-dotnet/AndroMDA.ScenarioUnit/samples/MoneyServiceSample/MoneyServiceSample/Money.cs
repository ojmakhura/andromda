using System;

namespace MoneyServiceSample
{
    public class Money
    {
        public int amount;
        public string currency;

        public Money() { }

        public Money(int amt, string crncy)
        {
            amount = amt;
            currency = crncy;
        }
    }
}
