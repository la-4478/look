package com.lookmarket.common.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class FileDownloadController {
    private static final String CURR_IMAGE_REPO_PATH = "C:\\lookmarket_resources\\file_repo";

    @RequestMapping("/download")
    protected void download(@RequestParam("i_file_name") String i_file_name,
                            @RequestParam(value = "g_id", required = false) String g_id,
                            HttpServletResponse response) throws Exception {

        OutputStream out = response.getOutputStream();

        String filePath;
        if (g_id == null || g_id.isEmpty()) {
            filePath = CURR_IMAGE_REPO_PATH + File.separator + i_file_name;
        } else {
            filePath = CURR_IMAGE_REPO_PATH + File.separator + g_id + File.separator + i_file_name;
        }

        File image = new File(filePath);

        response.setHeader("Cache-Control", "no-cache");
        response.addHeader("Content-disposition", "attachment; filename=" + i_file_name);

        FileInputStream in = new FileInputStream(image);
        byte[] buffer = new byte[1024 * 8];
        int count;
        while ((count = in.read(buffer)) != -1) {
            out.write(buffer, 0, count);
        }

        in.close();
        out.close();
    }
}
