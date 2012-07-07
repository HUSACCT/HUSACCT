using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.domain.hyves
{

    public class Krabbel : IObservable<Krabbel>
    {
        private string text;
        private List<IObserver<Krabbel>> observers;

        public String getText()
        {
            return text;
        }

        public void setText(String text)
        {
            this.text = text;
        }
        public IDisposable Subscribe(IObserver<Krabbel> observer)
        {
            if (!observers.Contains(observer))
                observers.Add(observer);
            return new Unsubscriber(observers, observer);
        }
    }

    public class Unsubscriber : IDisposable
    {
        private List<IObserver<Krabbel>> _observers;
        private IObserver<Krabbel> _observer;

        public Unsubscriber(List<IObserver<Krabbel>> observers, IObserver<Krabbel> observer)
        {
            this._observers = observers;
            this._observer = observer;
        }

        public void Dispose()
        {
            if (_observer != null && _observers.Contains(_observer))
                _observers.Remove(_observer);
        }
    }
}