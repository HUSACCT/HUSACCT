using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.domain.google_plus
{
    public class Circle : Observable
    {
        private List<Contact> contacts;

        public Circle()
        {
            contacts = new List<Contact>();
        }

        public void addContact(Contact contact)
        {
            contacts.Add(contact);
        }

        public void removeContact(Contact contact)
        {
            contacts.Remove(contact);
        }
    }
}