package design.cardia.ktxml.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

internal class DocumentTest {
    private val element = Element("test")
    private val subject = Document(XmlVersion.V1_0, XmlEncoding.UTF_8) { element }

    @Test
    fun `has XML instruction element with version and encoding`() {
        assertThat(subject.xmlInstructionElement["version"]).isEqualTo(XmlVersion.V1_0.value)
        assertThat(subject.xmlInstructionElement["encoding"]).isEqualTo(XmlEncoding.UTF_8.value)
    }

    @Test
    fun `has child element`() {
        assertThat(subject.child).isEqualTo(element)
    }
}
