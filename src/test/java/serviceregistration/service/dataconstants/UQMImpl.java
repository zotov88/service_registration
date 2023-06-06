package serviceregistration.service.dataconstants;

import serviceregistration.implquerymodel.UniversalQueryModelImpl;
import serviceregistration.querymodel.UniversalQueryModel;

import java.util.List;

public interface UQMImpl {

    UniversalQueryModelImpl UQMIMP_ONE = new UniversalQueryModelImpl();

    UniversalQueryModelImpl UQMIMP_TWO = new UniversalQueryModelImpl();

    UniversalQueryModelImpl UQMIMP_THREE = new UniversalQueryModelImpl();

    List<UniversalQueryModel> LIST_UQM = List.of(UQMIMP_ONE, UQMIMP_TWO, UQMIMP_THREE);

}
