package de.dhbw.elinor2;

import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.repositories.VirtualCashRegisterRepository;
import de.dhbw.elinor2.utils.GenericTest;
import de.dhbw.elinor2.utils.TestObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class VirtualCashRegisterTest extends GenericTest<VirtualCashRegister, VirtualCashRegister, UUID>
{
    @Autowired
    private VirtualCashRegisterRepository virtualCashRegisterRepository;

    private String BASE_URL = "http://localhost:8080/api/virtualcashregisters";


    @Override
    public String getObjectAssertionIdentificationSavedEntity(VirtualCashRegister virtualCashRegister)
    {
        return virtualCashRegister.getName();
    }

    @Override
    public String getObjectAssertionIdentificationReceivedEntity(VirtualCashRegister virtualCashRegister)
    {
        return getObjectAssertionIdentificationSavedEntity(virtualCashRegister);
    }

    @Override
    public TestObject<VirtualCashRegister, VirtualCashRegister, UUID> initTestObject()
    {
        TestObject<VirtualCashRegister, VirtualCashRegister, UUID> testObject = new TestObject<>();
        testObject.setRepository(virtualCashRegisterRepository);
        testObject.setBaseUrl(BASE_URL);
        testObject.setEntityClass(VirtualCashRegister.class);
        testObject.setEntityArrayClass(VirtualCashRegister[].class);

        VirtualCashRegister virtualCashRegister = new VirtualCashRegister();
        virtualCashRegister.setName("testName");
        virtualCashRegister = virtualCashRegisterRepository.save(virtualCashRegister);
        testObject.setInitSavedEntityId(virtualCashRegister.getId());
        testObject.setInitSavedEntity(virtualCashRegister);

        VirtualCashRegister updateVirtualCashRegister = new VirtualCashRegister();
        updateVirtualCashRegister.setName("updatedName");
        updateVirtualCashRegister.setId(virtualCashRegister.getId());
        testObject.setUpdateEntity(updateVirtualCashRegister);

        VirtualCashRegister newVirtualCashRegister = new VirtualCashRegister();
        newVirtualCashRegister.setName("newTestName");
        testObject.setNewEntity(newVirtualCashRegister);

        return testObject;
    }
}
