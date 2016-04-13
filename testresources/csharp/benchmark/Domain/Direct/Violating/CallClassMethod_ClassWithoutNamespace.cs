/* Sourcefile with multiple namespaces and multiple classes; classes with and without a namespace.
 * This file is used to determine the correct number of lines of code.
 * this class is also used to determine if classes without a namespace are correctly assigned.
 */

using Technology.Direct.Dao;

public class CallClassMethod_ClassWithoutNamespace1 {
	public CallClassMethod_ClassWithoutNamespace1 () {
		Technology.Direct.Dao.BadgesDAO.GetAllBadges();
	}
}

namespace Domain.Direct.Violating.Namespace1 {

    public interface IClassWithNamespace1 { }

	public class CallClassMethod_ClassWithNamespace1 {
		public CallClassMethod_ClassWithNamespace1 () {
			BadgesDAO.GetAllBadges();
		}
	}
}

namespace Domain.Direct.Violating.Namespace2 {

	using Domain.Direct.Violating.Namespace1;

	public class CallClassMethod_ClassWithNamespace2 : IClassWithNamespace1 {
		public CallClassMethod_ClassWithNamespace2 () {
			BadgesDAO.GetAllBadges();
		}
	}

	namespace Namespace2_1 {

		public class CallClassMethod_ClassWithNamespace2_1 : IClassWithNamespace1 {
			public CallClassMethod_ClassWithNamespace2_1 () {
				BadgesDAO.GetAllBadges();
			}
		}
	}
}

public class CallClassMethod_ClassWithoutNamespace2 : Domain.Direct.Violating.Namespace1.IClassWithNamespace1 {
	public CallClassMethod_ClassWithoutNamespace2 () {
		BadgesDAO.GetAllBadges();
	}
}

