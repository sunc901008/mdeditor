package focus.editor.controller;

import com.alibaba.fastjson.JSONObject;
import focus.editor.base.Common;
import focus.editor.base.StringUtils;
import focus.editor.entity.MdEditor;
import focus.editor.mapper.MdEditorMapper;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.UUID;

/**
 * @author sunc
 * @date 2019/10/9 10:36
 * @description UserController
 */
@Controller
public class RouteController {
    private static final Logger logger = Logger.getLogger(RouteController.class);

    @Resource
    private MdEditorMapper mdEditorMapper;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        logger.info("Get index html.");
        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        List<MdEditor> mds = mdEditorMapper.selectList(null);
        return JSONObject.toJSONString(mds);
    }

    @ResponseBody
    @RequestMapping(value = "/md/cache", method = RequestMethod.GET)
    public String get(HttpServletRequest request) {
        logger.info("Init get current cache data.");
        String ip = Common.getIpAddress(request);
        if (StringUtils.isBlank(ip)) {
            MdEditor md = mdEditorMapper.selectById(0);
            if (md == null) {
                return "";
            }
            return md.getContent();
        }
        MdEditor md = mdEditorMapper.selectByIp(ip);
        if (md == null) {
            md = mdEditorMapper.selectById(0);
            if (md == null) {
                return "";
            }
        }
        return md.getContent();
    }

    @ResponseBody
    @RequestMapping(value = "/md/cache", method = RequestMethod.POST)
    public String save(@RequestBody String data, HttpServletRequest request) {
        logger.info("Auto save current cache data.");
        if (StringUtils.isNotBlank(data)) {
            String ip = Common.getIpAddress(request);
            MdEditor md = mdEditorMapper.selectByIp(ip);
            if (md != null) {
                md.setContent(data);
                mdEditorMapper.updateById(md);
                logger.info("Update current cache success.");
            } else {
                MdEditor mdEditor = new MdEditor(data);
                mdEditor.setIp(ip);
                mdEditorMapper.insert(mdEditor);
                logger.info("Save current cache success.");
            }
        }
        return "success";
    }

    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final String WIN = "win";

    private static String PIC_SAVE_PATH = "/home/sunc/pic/";
    private static final String PIC_VIEW_PATH = "/md/pic/view/";

    static {
        logger.info("Current OS is: " + OS);
        if (OS.startsWith(WIN)) {
            PIC_SAVE_PATH = "G:/png/";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/md/upload/pic", method = RequestMethod.POST)
    public String picUpload(@RequestParam(value = "editormd-image-file") MultipartFile file) throws IOException {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        String originalFilename = file.getOriginalFilename();
        logger.info("upload img: " + originalFilename);
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = uuid + suffix;
        File targetFile = new File(PIC_SAVE_PATH, fileName);
        file.transferTo(targetFile);

        JSONObject res = new JSONObject();
        res.put("url", PIC_VIEW_PATH + fileName);
        res.put("success", 1);
        res.put("message", "upload success!");

        return res.toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "/md/pic/view/*", method = RequestMethod.GET)
    public void picView(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        String fileName = uri.substring(uri.lastIndexOf("\\") + 1);
        fileName = fileName.substring(uri.lastIndexOf("/") + 1);

        logger.info("view img: " + fileName);
        File file = new File(PIC_SAVE_PATH, fileName);

        if (!file.exists()) {
            logger.info("Can't find image file:" + file.getAbsolutePath());
            return;
        }

        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        response.setHeader("Content-Type", "image/" + fileType);

        byte[] buf = new byte[1024];
        int len;

        OutputStream res = response.getOutputStream();
        try (InputStream in = new FileInputStream(file)) {
            while ((len = in.read(buf)) > 0) {
                res.write(buf, 0, len);
            }
        }
    }

}
