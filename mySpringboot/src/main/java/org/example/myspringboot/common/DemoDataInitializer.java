package org.example.myspringboot.common;
import org.example.myspringboot.entity.*;
import org.example.myspringboot.mapper.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.annotation.Resource;
@Component
@ConditionalOnProperty(name="app.seed-demo", havingValue="true")
public class DemoDataInitializer implements ApplicationRunner {
    @Resource UserMapper users;
    @Resource MedicineInfoMapper catalogs;
    @Resource MedicineDrugInfoMapper drugs;
    @Value("${app.seed-confirm:}") String confirm;
    @Value("${app.demo-admin-password:}") String adminPassword;
    @Value("${app.demo-user-password:}") String userPassword;
    @Override @Transactional public void run(ApplicationArguments args) {
        if (!"EMPTY_DEMO_DATABASE".equals(confirm) || adminPassword.length() < 12 || userPassword.length() < 12)
            throw new IllegalStateException("Explicit demo confirmation and passwords of at least 12 characters required");
        if (users.selectCount(null) != 0 || catalogs.selectCount(null) != 0 || drugs.selectCount(null) != 0)
            throw new IllegalStateException("Seed only an empty demo database, never the original database");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        for (String role : new String[]{"admin", "user"}) {
            User user = new User(); user.setUsername("demo_" + role); user.setRole(role);
            user.setName("演示" + role); user.setPassword(encoder.encode("admin".equals(role) ? adminPassword : userPassword));
            users.insert(user);
        }
        MediCatalogInfo category = new MediCatalogInfo(); category.setId("DEMO-CAT-01");
        category.setOrgaId("0521"); category.setParentId("0"); category.setCatalogCode("DEMO");
        category.setCatalogName("演示药品"); category.setCatalogInputCode("YSYP"); catalogs.insert(category);
        for (int i = 1; i <= 3; i++) {
            MediDrugInfo drug = new MediDrugInfo(); drug.setId("DEMO-DRUG-0" + i); drug.setOrgaId("0521");
            drug.setCatalogID(category.getId()); drug.setDrugCode("DEMO00" + i); drug.setDrugName("虚拟演示药品" + i);
            drug.setDrugInputCode("YSYP" + i); drug.setDrugSpecification("演示规格");
            drug.setPriceUnitName("盒"); drug.setPriceUnitID("BOX");
            drug.setStockPackagQuantity(java.math.BigDecimal.ONE); drug.setOutpPackagQuantity(java.math.BigDecimal.ONE);
            drug.setStockPackagUnitID("BOX"); drug.setStockPackagUnitName("盒");
            drug.setOutpPackagUnitID("BOX"); drug.setOutpPackagUnitName("盒"); drug.setStatus("解冻"); drug.setOrigin("虚拟生产企业"); drug.setIsActive("是");
            drugs.insert(drug);
        }
    }
}
