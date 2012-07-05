using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CSharpBenchmark.presentation.gui.observer.google_plus;

namespace CSharpBenchmark.domain.google_plus
{

    public abstract class Observable
    {
        private List<presentation.gui.observer.google_plus.Observer> observers = new List<Observer>();

        public void attach(presentation.gui.observer.google_plus.Observer o)
        {
            observers.Add(o);
        }

        public void detach(Observer o)
        {
            observers.Remove(o);
        }

        public void notifyObservers()
        {
            foreach (Observer o in observers)
            {
                o.update();
            }
        }
    }
}