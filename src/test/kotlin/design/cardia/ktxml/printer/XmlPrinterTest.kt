package design.cardia.ktxml.printer

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.startsWith
import design.cardia.ktxml.builder.document
import design.cardia.ktxml.builder.element
import design.cardia.ktxml.model.CdataNode
import design.cardia.ktxml.model.Comment
import design.cardia.ktxml.model.Node
import design.cardia.ktxml.model.ProcessingInstructionElement
import design.cardia.ktxml.model.Text
import design.cardia.ktxml.model.XmlEncoding
import design.cardia.ktxml.model.XmlVersion
import org.junit.jupiter.api.Test

internal class XmlPrinterTest {
    private val elementPrinter = ElementPrinter()

    private val subject = XmlPrinter(elementPrinter)

    @Test
    fun `prints XML instruction element`() {
        val document = document()
        val format = PrettyFormat()

        val result = subject.print(document, format)

        assertThat(result).startsWith("""<?xml version="1.0" encoding="UTF-8" ?>""")
    }

    @Test
    fun `prints text element`() {
        val document = document(Text("hi"))
        val format = PrettyFormat()

        val result = subject.print(document, format)

        assertThat(result).contains("""<test>hi</test>""")
    }

    @Test
    fun `prints text element on new line`() {
        val document = document(Text("hi"))
        val format = PrettyFormat(putTextOnNewLine = true)

        val result = subject.print(document, format)

        assertThat(result).contains("<test>\n  hi\n</test>")
    }

    @Test
    fun `prints processing instruction element`() {
        val element = ProcessingInstructionElement("hi").apply {
            "hello" to "world"
        }

        val document = document(element)
        val format = PrettyFormat()

        val result = subject.print(document, format)

        assertThat(result).contains(
            """
            |<test>
            |  <?hi hello="world" ?>
            |</test>
            """.trimMargin()
        )
    }

    @Test
    fun `prints comment`() {
        val element = Comment("hello")

        val document = document(element)
        val format = PrettyFormat()

        val result = subject.print(document, format)

        assertThat(result).contains(
            """
            |<test>
            |  <!-- hello -->
            |</test>
            """.trimMargin()
        )
    }

    @Test
    fun `prints Cdata`() {
        val element = CdataNode("<br>hello")

        val document = document(element)
        val format = PrettyFormat()

        val result = subject.print(document, format)

        assertThat(result).contains(
            """
            |<test>
            |  <![CDATA[<br>hello]]>
            |</test>
            """.trimMargin()
        )
    }

    @Test
    fun `escapes Cdata`() {
        val element = CdataNode("<br>hello]]>")

        val document = document(element)
        val format = PrettyFormat()

        val result = subject.print(document, format)

        assertThat(result).contains(
            """
            |<test>
            |  <![CDATA[<br>hello]]&gt;]]>
            |</test>
            """.trimMargin()
        )
    }

    @Test
    fun `prints compressed`() {
        val element = CdataNode("<br>hello")

        val document = document(element)
        val format = CompressedFormat()

        val result = subject.print(document, format)

        assertThat(result).contains("<test><![CDATA[<br>hello]]></test>")
    }

    private fun document(node: Node? = null, version: XmlVersion = XmlVersion.V1_0, encoding: XmlEncoding = XmlEncoding.UTF_8) =
        document(version, encoding) {
            element("test") {
                node?.let(::add)
            }
        }
}
