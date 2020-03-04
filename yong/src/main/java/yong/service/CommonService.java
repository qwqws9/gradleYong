package yong.service;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import yong.common.Result;
import yong.dto.FileDto;
import yong.entity.FileEntity;
import yong.exception.BadRequestException;
import yong.jpa.FilesJpa;

@Service
@Slf4j
public class CommonService {

    @Autowired
    private FilesJpa filesJpa;
    
    /**
     * 
     * 이미지 업로드
     *
     * @since 2020. 3. 4.
     * @author yong
     *
     * @param request
     * @return
     */
    public Result fileUpload(MultipartHttpServletRequest request, String tempSeq) {
        // 폴더 명 생성
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
        ZonedDateTime current = ZonedDateTime.now();
        String folderName = current.format(format);

        // return 이미지명
        StringBuilder sb = new StringBuilder();
        List<String> pathList = new ArrayList<>();

        String path = request.getSession().getServletContext().getRealPath("resources/img/upload/") + folderName;

        // 폴더 없으면 생성
        File file = new File(path);
        if (file.exists() == false){
            file.mkdirs();
        }

        Iterator<String> iterator = request.getFileNames();

        String newFileName, originalFileExtension, contentType;

        while (iterator.hasNext()) {
            List<MultipartFile> list = request.getFiles(iterator.next());
            for (MultipartFile multipartFile : list) {
                if (multipartFile.isEmpty() == false) {
                    contentType = multipartFile.getContentType();
                    if (ObjectUtils.isEmpty(contentType)) {
                        break;
                    } else {
                        if (contentType.contains("image/jpeg")) {
                            originalFileExtension = ".jpg";
                        } else if (contentType.contains("image/png")) {
                            originalFileExtension = ".png";
                        } else if (contentType.contains("image/gif")) {
                            originalFileExtension = ".gif";
                        } else {
                            break;
                        }
                    }

                    newFileName = Long.toString(System.nanoTime()) + originalFileExtension;

                    sb.setLength(0);
                    sb.append(folderName);
                    sb.append("/");
                    sb.append(newFileName);

                    pathList.add(sb.toString());
                    file = new File(path + "/" + newFileName);

                    try {
                        multipartFile.transferTo(file);
                        FileDto fileDto = new FileDto();
                        fileDto.setDelYn("Y");
                        // 나중에 게시판이 추가될경우 master_seq 추가하여 변경하기
                        fileDto.setMasterSeq(1);
                        fileDto.setFilePath(sb.toString());
                        fileDto.setFileSize(multipartFile.getSize());
                        fileDto.setBoardSeq(Long.valueOf(tempSeq));
                        fileDto.setOriginalFileName(multipartFile.getOriginalFilename());
                        filesJpa.save(fileDto.toEntity(FileEntity.class));
                    } catch (Exception e) {
                        throw new BadRequestException(e);
                    }
                }
            }
        }

        return pathList.size() == 0 ? new Result("업로드 실패") : new Result(pathList);
    }
}
