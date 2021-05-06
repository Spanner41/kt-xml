package design.cardia.ktxml.builder

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ElementTest {
    @Nested
    inner class WithPrettyPrint {
        @Test
        fun `prints nested indentation`() {
            val xml = element("first") {
                element("second") {}
            }

            val format = PrettyFormat()
            assertThat(xml.toXml(format, XmlVersion.V1_1, XmlEncoding.UTF_8)).isEqualTo(
                """
                    
                    <first>
                      <second />
                    </first>
                """.trimIndent()
            )
        }

        @Test
        fun `by default prints text node on one line`() {
            val xml = element("first") withText "test"

            val format = PrettyFormat()
            assertThat(xml.toXml(format, XmlVersion.V1_1, XmlEncoding.UTF_8))
                .isEqualTo("\n<first>test</first>")
        }

        @Test
        fun `can print text on multiple lines`() {
            val xml = element("first") withText "test"

            val format = PrettyFormat(textOnNewLine = true)
            assertThat(xml.toXml(format, XmlVersion.V1_1, XmlEncoding.UTF_8))
                .isEqualTo("\n<first>\n  test\n</first>")
        }
    }
}