package design.cardia.ktxml.printer

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import design.cardia.ktxml.builder.element
import design.cardia.ktxml.builder.text
import design.cardia.ktxml.model.XmlVersion
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ElementPrinterTest {
    private val version = XmlVersion.V1_0

    private val subject = ElementPrinter()

    @Test
    fun `prints children`() {
        val element = element("type") {
            element("first")
            element("second")
        }
        val format = PrettyFormat(collapseEmptyTags = true)
        var callCount = 0

        val result = subject.print(element, format, version, "", "") {
            callCount++
            "content"
        }

        assertThat(callCount).isEqualTo(2)
        assertThat(result).isEqualTo("<type>contentcontent</type>")
    }

    @Test
    fun `prints separator between children and ends with postfix`() {
        val element = element("type") {
            element("first")
            element("second")
        }
        val format = PrettyFormat(collapseEmptyTags = true)
        var callCount = 0

        val result = subject.print(element, format, version, "\n  ", "\n") {
            callCount++
            "content"
        }

        assertThat(callCount).isEqualTo(2)
        assertThat(result).isEqualTo("<type>\n  content\n  content\n</type>")
    }

    @Test
    fun `prints children on one line when configured and at least one is a text`() {
        val element = element("type") {
            element("first")
            text("hi")
            element("second")
        }
        val format = PrettyFormat(putTextOnNewLine = false)
        var callCount = 0

        val result = subject.print(element, format, version, "\n  ", "\n") {
            callCount++
            "content"
        }

        assertThat(callCount).isEqualTo(3)
        assertThat(result).isEqualTo("<type>contentcontentcontent</type>")
    }

    @Test
    fun `prints children separate lines when configured`() {
        val element = element("type") {
            element("first")
            text("hi")
            element("second")
        }
        val format = PrettyFormat(putTextOnNewLine = true)
        var callCount = 0

        val result = subject.print(element, format, version, "\n  ", "\n") {
            callCount++
            "content"
        }

        assertThat(callCount).isEqualTo(3)
        assertThat(result).isEqualTo("<type>\n  content\n  content\n  content\n</type>")
    }

    @Nested
    inner class Collapsing {
        @Test
        fun `element with no children prints collapsed`() {
            val element = element("type") {
                "hello" to "world"
                "collapsed" to "tag"
            }
            val format = PrettyFormat(collapseEmptyTags = true)
            var childIsNotPrinted = true

            val result = subject.print(element, format, version, "", "") {
                childIsNotPrinted = false
                "content"
            }

            assertThat(childIsNotPrinted)
            assertThat(result).isEqualTo("""<type hello="world" collapsed="tag" />""")
        }

        @Test
        fun `element expands according to format`() {
            val element = element("type") {
                "hello" to "world"
                "collapsed" to "tag"
            }
            val format = PrettyFormat(collapseEmptyTags = false)
            var childIsNotPrinted = true

            val result = subject.print(element, format, version, "", "") {
                childIsNotPrinted = false
                "content"
            }

            assertThat(childIsNotPrinted)
            assertThat(result).isEqualTo("""<type hello="world" collapsed="tag"></type>""")
        }

        @Test
        fun `element with children prints expanded`() {
            val element = element("type") {
                "hello" to "world"
                "collapsed" to "tag"

                text("")
            }
            val format = PrettyFormat(collapseEmptyTags = true)
            var childIsNotPrinted = true

            val result = subject.print(element, format, version, "", "") {
                childIsNotPrinted = false
                "content"
            }

            assertThat(childIsNotPrinted).isFalse()
            assertThat(result).isEqualTo("""<type hello="world" collapsed="tag">content</type>""")
        }
    }
}
