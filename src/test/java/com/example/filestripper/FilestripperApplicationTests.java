package com.example.filestripper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")//This is for intellij warning
class FilestripperApplicationTests {

    @Autowired
    private FileStripperController controller;

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGet() throws Exception {
        this.mockMvc.perform(get("/file/extract")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Post to this URL with file param 'file'")));
    }

    @Test
    public void testFile() {
        String path = "./src/test/resources";

        File docFile = new File(path, "test.doc");
        File docxFile = new File(path, "test.docx");
        File pdfFile = new File(path, "test.pdf");

        assertTrue(docFile.exists());
        assertTrue(docxFile.exists());
        //assertTrue(pdfFile.exists());
    }

    private static final String path = "./src/test/resources";
    @Test
    public void testDocExtraction() throws Exception {

        File docFile = new File(path, "test.doc");
        MockMultipartFile docFileMock = new MockMultipartFile("file", docFile.getName(), "multipart/form-data", new FileInputStream(docFile));

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/file/extract").file(docFileMock))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("docfile")));
    }

    @Test
    public void testDocxExtraction() throws Exception {

        File docFile = new File(path, "test.docx");
        MockMultipartFile docFileMock = new MockMultipartFile("file", docFile.getName(), "multipart/form-data", new FileInputStream(docFile));

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/file/extract").file(docFileMock))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("docxfile")));
    }
}
