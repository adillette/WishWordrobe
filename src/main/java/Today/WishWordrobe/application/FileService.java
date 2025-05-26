package Today.WishWordrobe.application;

import Today.WishWordrobe.domain.ClothesImageUploadInfo;
import Today.WishWordrobe.domain.FileInfo;
import Today.WishWordrobe.domain.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.tomcat.util.http.fileupload.FileUploadException;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
import java.util.stream.Collectors;
import java.io.File;


@Service
@RequiredArgsConstructor
@Profile("dev")
public class FileService {

    @Value("${wish.local.file.base.directory:./clothesUploads}")
    private  String baseDir;

    public FileInfo uploadFile(MultipartFile file, String userId) 
                throws FileUploadException{
        
        String newFileName = FileUtil.changeFileName(file);
        
        checkDirectory(userId);

        return createFileInfo(file, userId, newFileName);
    }

    public List<FileInfo> uploadFiles(List<MultipartFile> files,
                                      String userId) throws FileUploadException{
        HashMap<String, String> newFileNames = FileUtil.changeFileNames(files);

        checkDirectory(userId);

        List<FileInfo> fileInfos = files.stream()
                .map(file-> createFileInfo(file,userId,newFileNames.get(file.getOriginalFilename())))
                .collect(Collectors.toList());
        return fileInfos;

    }

    public void uploadImage(Long id, FileInfo fileInfo){
        ClothesImageUploadInfo imageUploadInfo =
                FileUtil.toImageUploadInfo(id,fileInfo,1);
        fileMapper.insertImage(imageUploadInfo);
    }

    public void uploadImages(Long id, List<FileInfo> fileInfos){
        List<ClothesImageUploadInfo> imageUploadInfos = fileInfos.stream()
                .map(info -> FileUtil.toImageUploadInfo(id, info,fileInfos.indexOf(info)+1 ))
                .collect(Collectors.toList());
        fileMapper.insertImages(imageUploadInfos);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isExistImages(int postId) {

        return fileMapper.isExistImages(postId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Image> getImages(int postId) {

        return fileMapper.getImages(postId);
    }

    @Override
    public void deleteFile(String filePath) {

        if (filePath != null) {
            new File(filePath).delete();
        }
    }

    @Override
    public void deleteDirectory(String userId) throws FileDeleteException {

        StringBuilder dirPath = new StringBuilder()
                .append(baseDir)
                .append(File.separator)
                .append(userId);

        boolean isSuccess = FileSystemUtils.deleteRecursively(new File(String.valueOf(dirPath)));

        if (!isSuccess) {
            throw new FileDeleteException("파일을 삭제하는데 실패하였습니다.");
        }
    }

    @Override
    public void deleteImages(int postId) {
        List<String> imagePaths = fileMapper.getImagePaths(postId);

        imagePaths.stream().forEach(this::deleteFile);

        fileMapper.deleteImages(postId);
    }

    private void checkDirectory(String userId) {

        StringBuilder dirPath = new StringBuilder()
                .append(baseDir)
                .append(File.separator)
                .append(userId);

        File directory = new File(String.valueOf(dirPath));

        if (!directory.exists()) {
            directory.mkdir();
        }
    }


    private void checkDirectory(String userId) {
        StringBuilder dirPath = new StringBuilder()
                .append(baseDir)
                .append(File.separator)
                .append(userId);

        File directory = new File(String.valueOf(dirPath));

        if(!directory.exists()){
            directory.mkdir();
        }
    }


}
