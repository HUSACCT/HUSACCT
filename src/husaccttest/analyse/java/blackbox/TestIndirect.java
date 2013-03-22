package husaccttest.analyse.java.blackbox;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import static org.junit.Assert.*;

import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.TestCaseExtended;

public class TestIndirect extends TestCaseExtended {

    @Test
    public void testZinloos() {
        assertEquals(true, true);
    }

    public void testClassInterfaceInterface1FromInpackage() {
        String from = "indirect.houses.type.RowHouse";
        int indirectExpected = 1;

        DependencyDTO[] dependencies = service.getDependenciesFrom(from);
        assertEquals(indirectExpected, getIndirect(dependencies).size());

        String fromPathExpected = from;
        String toPathExpected = "indirect.houses.type.IGlobal";
        String typeExpected = super.EXTENDSCONCRETE;
        int linenumberExpected = 3;
        boolean isIndirectExpected = true;

        HashMap<String, Object> expectedindirectDependency = createDependencyHashmap(
                fromPathExpected, toPathExpected, typeExpected, linenumberExpected, isIndirectExpected);

        boolean found = compaireDTOWithValues(expectedindirectDependency, dependencies);
        assertEquals(true, found);
    }

    public void testClassInterfaceInterface1ToInAndOuterpackage() {
        String to = "indirect.houses.type.IGlobal";
        int indirectExpected = 3;

        DependencyDTO[] dependencies = service.getDependenciesTo(to);
        assertEquals(indirectExpected, getIndirect(dependencies).size());

        String fromPathExpected = "indirect.houses.type.RowHouse";
        String toPathExpected = to;
        String typeExpected = super.EXTENDSCONCRETE;
        int linenumberExpected = 3;
        boolean isIndirectExpected = true;

        String fromDoorPathExpected = "indirect.houses.attribute.Door";
        String toDoorPathExpected = to;
        String typeDoorExpected = super.EXTENDSCONCRETE;
        int linenumberDoorExpected = 3;
        boolean isIndirectDoorExpected = true;

        String fromIAttributePathExpected = "indirect.houses.attribute.IAttribute";
        String toIAttributePathExpected = to;
        String typeIAttributeExpected = super.EXTENDSCONCRETE;
        int linenumberIAttributeExpected = 5;
        boolean isIndirectIAttributeExpected = true;

        HashMap<String, Object> expectedindirectDependency = createDependencyHashmap(
                fromPathExpected, toPathExpected, typeExpected, linenumberExpected, isIndirectExpected);
        HashMap<String, Object> expectedDoorDependency = createDependencyHashmap(
                fromDoorPathExpected, toDoorPathExpected, typeDoorExpected, linenumberDoorExpected, isIndirectDoorExpected);
        HashMap<String, Object> expectedIAttributeDependency = createDependencyHashmap(
                fromIAttributePathExpected, toIAttributePathExpected, typeIAttributeExpected, linenumberIAttributeExpected, isIndirectIAttributeExpected);

        boolean found = compaireDTOWithValues(expectedindirectDependency, dependencies);
        boolean foundDoor = compaireDTOWithValues(expectedDoorDependency, dependencies);
        boolean foundIAttribute = compaireDTOWithValues(expectedIAttributeDependency, dependencies);
        assertEquals(true, found);
        assertEquals(true, foundDoor);
        assertEquals(true, foundIAttribute);
    }

    public void testClassInterfaceInterface1FromToInpackage() {
        String from = "indirect.houses.type.RowHouse";
        String to = "indirect.houses.type.IGlobal";
        int indirectExpected = 1;

        DependencyDTO[] dependencies = service.getDependenciesFrom(from);
        assertEquals(indirectExpected, getIndirect(dependencies).size());

        String fromPathExpected = from;
        String toPathExpected = "indirect.houses.type.IGlobal";
        String typeExpected = super.EXTENDSCONCRETE;
        int linenumberExpected = 3;
        boolean isIndirectExpected = true;

        HashMap<String, Object> expectedindirectDependency = createDependencyHashmap(
                fromPathExpected, toPathExpected, typeExpected, linenumberExpected, isIndirectExpected);

        boolean found = compaireDTOWithValues(expectedindirectDependency, dependencies);
        assertEquals(true, found);
    }

    public void testClassInterfaceInterface1FromOuterpackage() {
        String from = "indirect.houses.attribute.Door";
        int indirectExpected = 2;

        DependencyDTO[] dependencies = service.getDependenciesFrom(from);
        assertEquals(indirectExpected, getIndirect(dependencies).size());

        String fromITypePathExpected = from;
        String toITypePathExpected = "indirect.houses.type.IType";
        String typeITypeExpected = super.EXTENDSCONCRETE;
        int linenumberITypeExpected = 3;
        boolean isIndirectITypeExpected = true;

        String fromIGlobalPathExpected = from;
        String toIGlobalPathExpected = "indirect.houses.type.IGlobal";
        String typeIGlobalExpected = super.EXTENDSCONCRETE;
        int linenumberIGlobalExpected = 3;
        boolean isIndirectIGlobalExpected = true;

        HashMap<String, Object> expectedITypeDependency = createDependencyHashmap(
                fromITypePathExpected, toITypePathExpected, typeITypeExpected, linenumberITypeExpected, isIndirectITypeExpected);
        HashMap<String, Object> expectedIGlobalDependency = createDependencyHashmap(
                fromIGlobalPathExpected, toIGlobalPathExpected, typeIGlobalExpected, linenumberIGlobalExpected, isIndirectIGlobalExpected);

        boolean foundIType = compaireDTOWithValues(expectedITypeDependency, dependencies);
        boolean foundIGlobal = compaireDTOWithValues(expectedIGlobalDependency, dependencies);
        assertEquals(true, foundIType);
        assertEquals(true, foundIGlobal);
    }

    public void testClassInterfaceInterface1FromToOuterpackage() {
        String from = "indirect.houses.attribute.Door";
        String to = "indirect.houses.type.IGlobal";
        int indirectExpected = 1;

        DependencyDTO[] dependencies = service.getDependencies(from, to);
        assertEquals(indirectExpected, getIndirect(dependencies).size());

        String fromIGlobalPathExpected = from;
        String toIGlobalPathExpected = to;
        String typeIGlobalExpected = super.EXTENDSCONCRETE;
        int linenumberIGlobalExpected = 3;
        boolean isIndirectIGlobalExpected = true;

        HashMap<String, Object> expectedIGlobalDependency = createDependencyHashmap(
                fromIGlobalPathExpected, toIGlobalPathExpected, typeIGlobalExpected, linenumberIGlobalExpected, isIndirectIGlobalExpected);

        boolean foundIGlobal = compaireDTOWithValues(expectedIGlobalDependency, dependencies);
        assertEquals(true, foundIGlobal);
    }

    private ArrayList<DependencyDTO> getIndirect(DependencyDTO[] dependencies) {
        ArrayList<DependencyDTO> indirect = new ArrayList<DependencyDTO>();
        for (DependencyDTO d : dependencies) {
            if (d.isIndirect) {
                indirect.add(d);
            }
        }
        return indirect;
    }
}
