package ch.bus.rental.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CustomPhysicalNamingStrategyTest {

    private CustomPhysicalNamingStrategy strategy;

    @Mock
    private JdbcEnvironment jdbcEnvironment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        strategy = new CustomPhysicalNamingStrategy();
    }

    @Nested
    @DisplayName("Table Name Tests")
    class TableNameTests {

        @Test
        @DisplayName("Should convert DBO class to T_ prefix table name")
        void shouldConvertDboClassToTableName() {
            // Arrange
            Identifier identifier = Identifier.toIdentifier("CustomerDbo");

            // Act
            Identifier result = strategy.toPhysicalTableName(identifier, jdbcEnvironment);

            // Assert
            Assertions.assertEquals("T_CUSTOMER", result.getText());
        }

        @Test
        @DisplayName("Should convert View class to V_ prefix table name")
        void shouldConvertViewClassToTableName() {
            // Arrange
            Identifier identifier = Identifier.toIdentifier("CustomerView");

            // Act
            Identifier result = strategy.toPhysicalTableName(identifier, jdbcEnvironment);

            // Assert
            Assertions.assertEquals("V_CUSTOMER", result.getText());
        }

        @Test
        @DisplayName("Should add T_ prefix to regular class name")
        void shouldAddPrefixToRegularClassName() {
            // Arrange
            Identifier identifier = Identifier.toIdentifier("Customer");

            // Act
            Identifier result = strategy.toPhysicalTableName(identifier, jdbcEnvironment);

            // Assert
            Assertions.assertEquals("T_CUSTOMER", result.getText());
        }
    }

    @Nested
    @DisplayName("Column Name Tests")
    class ColumnNameTests {

        @Test
        @DisplayName("Should convert camelCase to SNAKE_CASE")
        void shouldConvertCamelCaseToSnakeCase() {
            // Arrange
            Identifier identifier = Identifier.toIdentifier("firstName");

            // Act
            Identifier result = strategy.toPhysicalColumnName(identifier, jdbcEnvironment);

            // Assert
            Assertions.assertEquals("FIRST_NAME", result.getText());
        }

        @Test
        @DisplayName("Should handle multiple uppercase letters")
        void shouldHandleMultipleUppercaseLetters() {
            // Arrange
            Identifier identifier = Identifier.toIdentifier("customerSSN");

            // Act
            Identifier result = strategy.toPhysicalColumnName(identifier, jdbcEnvironment);

            // Assert
            Assertions.assertEquals("CUSTOMER_SSN", result.getText());
        }

        @Test
        @DisplayName("Should handle already uppercase words")
        void shouldHandleAlreadyUppercaseWords() {
            // Arrange
            Identifier identifier = Identifier.toIdentifier("CUSTOMER");

            // Act
            Identifier result = strategy.toPhysicalColumnName(identifier, jdbcEnvironment);

            // Assert
            Assertions.assertEquals("CUSTOMER", result.getText());
        }

        @Test
        @DisplayName("Should handle single character")
        void shouldHandleSingleCharacter() {
            // Arrange
            Identifier identifier = Identifier.toIdentifier("a");

            // Act
            Identifier result = strategy.toPhysicalColumnName(identifier, jdbcEnvironment);

            // Assert
            Assertions.assertEquals("A", result.getText());
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle class name ending with 'DboView'")
        void shouldHandleClassNameEndingWithDboView() {
            // Arrange
            Identifier identifier = Identifier.toIdentifier("CustomerDboView");

            // Act
            Identifier result = strategy.toPhysicalTableName(identifier, jdbcEnvironment);

            // Assert
            Assertions.assertEquals("V_CUSTOMERDBO", result.getText());
        }

        @Test
        @DisplayName("Should handle class name with multiple 'Dbo' occurrences")
        void shouldHandleClassNameWithMultipleDboOccurrences() {
            // Arrange
            Identifier identifier = Identifier.toIdentifier("DboCustomerDbo");

            // Act
            Identifier result = strategy.toPhysicalTableName(identifier, jdbcEnvironment);

            // Assert
            Assertions.assertEquals("T_DBOCUSTOMER", result.getText());
        }

        @Test
        @DisplayName("Should handle null table identifier")
        void shouldHandleNullTableIdentifier() {
            // Act
            Identifier result = strategy.toPhysicalTableName(null, jdbcEnvironment);

            // Assert
            Assertions.assertNull(result);
        }

        @Test
        @DisplayName("Should handle empty string table identifier")
        void shouldHandleEmptyStringTableIdentifier() {
            // Arrange
            Identifier identifier = Identifier.toIdentifier("");

            // Act
            Identifier result = strategy.toPhysicalTableName(identifier, jdbcEnvironment);

            // Assert
            Assertions.assertNull(result);
        }

        @Test
        @DisplayName("Should handle null column identifier")
        void shouldHandleNullColumnIdentifier() {
            // Act
            Identifier result = strategy.toPhysicalColumnName(null, jdbcEnvironment);

            // Assert
            Assertions.assertNull(result);
        }

        @Test
        @DisplayName("Should handle empty string column identifier")
        void shouldHandleEmptyStringColumnIdentifier() {
            // Arrange
            Identifier identifier = Identifier.toIdentifier("");

            // Act
            Identifier result = strategy.toPhysicalColumnName(identifier, jdbcEnvironment);

            // Assert
            Assertions.assertNull(result);
        }
    }
}
