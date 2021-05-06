package design.cardia.ktxml.builder

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

internal class CdataNodeTest {
    @Test
    fun `prints opening and closing tags`() {
        val subject = CdataNode("test")

        val result = subject.toXml(PrettyFormat(), XmlVersion.V1_1, XmlEncoding.UTF_8)

        assertThat(result).isEqualTo("<![CDATA[test]]>")
    }

    @Test
    fun `escapes closing sequence if in text`() {
        val subject = CdataNode("]]>")

        val result = subject.toXml(PrettyFormat(), XmlVersion.V1_1, XmlEncoding.UTF_8)

        assertThat(result).isEqualTo("<![CDATA[]]&gt;]]>")
    }
}
