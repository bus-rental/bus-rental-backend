package ch.bus.rental.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.stereotype.Component;

@Component
public class CustomPhysicalNamingStrategy extends PhysicalNamingStrategyStandardImpl {

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        // Handle null case
        if (name == null) {
            return null;
        }

        String originalName = name.getText();
        // Handle empty string case
        if (originalName == null || originalName.isEmpty()) {
            return name;
        }

        String newName;
        if (originalName.endsWith("Dbo")) {
            newName = "T_" + originalName.substring(0, originalName.length() - 3).toUpperCase();
        } else if (originalName.endsWith("View")) {
            newName = "V_" + originalName.substring(0, originalName.length() - 4).toUpperCase();
        } else {
            newName = "T_" + originalName.toUpperCase();
        }

        return Identifier.toIdentifier(newName);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        // Handle null case
        if (name == null) {
            return null;
        }

        String originalName = name.getText();
        // Handle empty string case
        if (originalName == null || originalName.isEmpty()) {
            return name;
        }

        String newName = originalName.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
        return Identifier.toIdentifier(newName);
    }
}
