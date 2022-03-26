package coza.opencollab.epub.creator;

import coza.opencollab.epub.creator.api.MetadataItem;
import coza.opencollab.epub.creator.model.EpubBook;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.zip.ZipFile;

/**
 * @author OpenCollab
 */
public class EpubCreatorTest implements WithAssertions {

    String author = "Samuel Holtzkampf";
    String modified = "modified-date-and-time";

    @Test
    public void bookHasAuthor() {
        //when
        val book = createEpubBook();
        //then
        assertThat(book.getAuthor()).isEqualTo(author);

    }

    @Test
    public void hasSetModifiedValue() {
        //given
        //TODO use a proper temp file
        val file = new File("test.epub");
        writeBookToFile(createEpubBook(), file);
        //when
        String bookOpf = unzipFileEntry(file, "content/book.opf");
        //then
        assertThat(bookOpf).containsOnlyOnce("<meta property=\"dcterms:modified\">");
        assertThat(bookOpf).contains(String.format("<meta property=\"dcterms:modified\">%s</meta>", modified));
    }

    @SneakyThrows
    private String unzipFileEntry(File file, String name) {
        val zipFile = new ZipFile(file);
        val entry = zipFile.getEntry(name);
        val inputStream = zipFile.getInputStream(entry);
        try (Scanner scanner = new Scanner(inputStream)) {
            return scanner.useDelimiter("\\A").next();
        }
    }

    @SneakyThrows
    private void writeBookToFile(EpubBook book, File file) {
        try (OutputStream outputStream = new FileOutputStream(file)) {
            book.writeToStream(outputStream);
        }
    }

    @SneakyThrows
    private EpubBook createEpubBook() {
        EpubBook book = new EpubBook("en", "Samuel .-__Id1", "Samuel Test Book", author);

        MetadataItem.Builder builder = MetadataItem.builder();
        book.addMetadata(builder.name("dc:creator").value("Bob Smith"));
        book.addMetadata(builder.name("meta")
                .property("role").refines("#editor-id")
                .value("Editor"));
        book.addMetadata((builder.name("meta").property("dcterms:modified").value(modified)));

        book.addContent(this.getClass().getResourceAsStream("/epub30-overview.xhtml"),
                "application/xhtml+xml", "xhtml/epub30-overview.xhtml", true, true).setId("Overview");
        book.addContent(this.getClass().getResourceAsStream("/idpflogo_web_125.jpg"),
                "image/jpeg", "img/idpflogo_web_125.jpg", false, false);
        book.addContent(this.getClass().getResourceAsStream("/epub-spec.css"),
                "text/css", "css/epub-spec.css", false, false);
        book.addTextContent("TestHtml", "xhtml/samuelTest2.xhtml", "Samuel test one two four!!!!!\nTesting two").setToc(true);
        book.addTextContent("TestHtml", "xhtml/samuelTest.xhtml", "Samuel test one two three\nTesting two").setToc(true);
        book.addCoverImage(IOUtils.toByteArray(this.getClass().getResourceAsStream("/P1010832.jpg")),
                "image/jpeg", "images/P1010832.jpg");
        return book;
    }
}
