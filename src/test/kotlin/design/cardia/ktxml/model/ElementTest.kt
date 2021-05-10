package design.cardia.ktxml.model

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

internal class ElementTest {
    private val subject = Element("test")

    @Test
    fun `adds a node`() {
        val node = Comment("this is a comment")

        subject.add(node)

        assertThat(subject.children).contains(node)
    }

    @Test
    fun `adds a node from a supplier`() {
        val node = Comment("this is another comment")

        subject.add { node }

        assertThat(subject.children).contains(node)
    }

    @Test
    fun `adds text`() {
        subject.withText("hi")

        val text = subject.children.filterIsInstance<Text>().firstOrNull()?.text

        assertThat(text).isEqualTo("hi")
    }
}
