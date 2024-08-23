package com.blog.service.impl;
import com.blog.entity.Category;
import com.blog.entity.Post;
import com.blog.entity.User;
import com.blog.exception.ResourceNotFoundException;
import com.blog.playload.CategoryDto;
import com.blog.playload.PostDto;
import com.blog.playload.UserDto;
import com.blog.repository.CategoryRepo;
import com.blog.repository.PostRepo;
import com.blog.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class PostService implements com.blog.service.PostService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public ResponseEntity<PostDto> createPost(PostDto postDto, MultipartFile multipartFile) throws IOException {

        String categoryName=postDto.getCategoryDto().getCategoryName();
        String name=postDto.getUserDto().getUserName();

        Category category=categoryRepo.findById(categoryName).orElseThrow(()-> new ResourceNotFoundException("Category","category Name",categoryName));

        User user=userRepository.findByUserName(name)
                .orElseThrow(()-> new ResourceNotFoundException("User","UserName",name));


        Post post=this.modelMapper.map(postDto, Post.class);

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        post.setImage(fileName);
        String uploadDir = "post-photos/" + post.getTitle();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        post.setAddedDate(new Date());
        post.setCategory(category);
        post.setUser(user);

        PostDto dto=modelMapper.map(postRepo.save(post),PostDto.class);

        dto.setCategoryDto(modelMapper.map(category,CategoryDto.class));
        dto.setUserDto(modelMapper.map(user,UserDto.class));

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PostDto> updatePost(PostDto postDto, MultipartFile multipartFile,String title) throws IOException
    {
        String categoryName=postDto.getCategoryDto().getCategoryName();
        String name=postDto.getUserDto().getUserName();

        Post post=this.postRepo.findById(title)
                .orElseThrow(()->new ResourceNotFoundException("Post","Title",title));

        Category category=categoryRepo.findById(categoryName)
                .orElseThrow(()-> new ResourceNotFoundException("Category","category Name",categoryName));

        User user=userRepository.findByUserName(name)
                .orElseThrow(()-> new ResourceNotFoundException("User","UserName",name));

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        post.setImage(fileName);
        String uploadDir = "post-photos/" + post.getTitle();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        post.setAddedDate(new Date());
        post.setUser(user);
        post.setCategory(category);

        PostDto dto=modelMapper.map(postRepo.save(post),PostDto.class);

        dto.setCategoryDto(modelMapper.map(category,CategoryDto.class));
        dto.setUserDto(modelMapper.map(user,UserDto.class));
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<PostDto> getPostByTitle(String title) {

        Post post=this.postRepo.findById(title).orElseThrow(()-> new ResourceNotFoundException("Post","title",title));
        PostDto postDto=modelMapper.map(post,PostDto.class);
        postDto.setUserDto(modelMapper.map(post.getUser(),UserDto.class));
        postDto.setCategoryDto(modelMapper.map(post.getCategory(),CategoryDto.class));

        return ResponseEntity.ok(postDto);
    }

    @Override
    public Page<PostDto> getAllPost(Pageable pageable) {
        // Fetch the paginated results from the repository
        Page<Post> postsPage = postRepo.findAll(pageable);

        // Convert the Page<Post> to Page<PostDto>
        return postsPage.map(post -> {
            PostDto postDto = modelMapper.map(post, PostDto.class);
            postDto.setCategoryDto(modelMapper.map(post.getCategory(), CategoryDto.class));
            postDto.setUserDto(modelMapper.map(post.getUser(), UserDto.class));
            return postDto;
        });
    }


    @Override
    public String deletePostByTitle(String title) {
        this.postRepo.findById(title).orElseThrow(()->new ResourceNotFoundException("Post","Title",title));
        postRepo.deleteById(title);
        return "Post with title :"+title+" Deleted Successfully..!!";
    }

    @Override
    public String deleteAll() {
        try{
            postRepo.deleteAll();
            return "All posts deleted Successfully..!!";
        }
        catch (Exception ex)
        {
            return ex.getLocalizedMessage();
        }
    }

    @Override
    public List<PostDto> getPostByUserName(String name) {
        return userRepository.findByUserName(name)
                .orElseThrow(()-> new ResourceNotFoundException("User","UserName",name))
                .getPost().stream()
                .map(post ->
                {
                    PostDto postDto= modelMapper.map(post,PostDto.class);
                    postDto.setUserDto(modelMapper.map(post.getUser(),UserDto.class));
                    postDto.setCategoryDto(modelMapper.map(post.getCategory(),CategoryDto.class));
                    return  postDto;
                })
                .toList();

    }

    @Override
    public List<PostDto> getPostByCategory(String categoryName) {
        return categoryRepo.findById(categoryName)
                .orElseThrow(()-> new ResourceNotFoundException("Category","category Name",categoryName))
                .getPost().stream()
                .map(post -> {
                            PostDto postDto= modelMapper.map(post,PostDto.class);
                            postDto.setUserDto(modelMapper.map(post.getUser(),UserDto.class));
                            postDto.setCategoryDto(modelMapper.map(post.getCategory(),CategoryDto.class));
                            return  postDto;
                        })
                .toList();
    }

    @Override
    public UserDto getUserByPost(String title) {
        return modelMapper.map(postRepo.findById(title)
                .orElseThrow(()->new ResourceNotFoundException("Post","Title",title))
                .getUser(),UserDto.class);

    }

    @Override
    public CategoryDto getCategoryByPost(String title) {
        return modelMapper.map(postRepo.findById(title)
                .orElseThrow(()->new ResourceNotFoundException("Post","Title",title))
                        .getCategory()
                ,CategoryDto.class);

    }

    @Override
    public List<PostDto> searchPosts(String keyword)
    {

        return postRepo.findByTitleContaining(keyword).stream()
                .map(post -> {
            PostDto postDto= modelMapper.map(post,PostDto.class);
            postDto.setUserDto(modelMapper.map(post.getUser(),UserDto.class));
            postDto.setCategoryDto(modelMapper.map(post.getCategory(),CategoryDto.class));
            return  postDto;

        })
                .toList();
    }
    @Override
    public List<PostDto> searchByCategory(String keyword)
    {

        return postRepo.findByCategory_CategoryNameContaining(keyword).stream()
                .map(post -> {
                    PostDto postDto= modelMapper.map(post,PostDto.class);
                    postDto.setUserDto(modelMapper.map(post.getUser(),UserDto.class));
                    postDto.setCategoryDto(modelMapper.map(post.getCategory(),CategoryDto.class));
                    return  postDto;

                })
                .toList();
    }

    @Override
    public List<PostDto> searchByUser(String keyword)
    {

        return postRepo.findByUser_UserNameContaining(keyword).stream()
                .map(post -> {
                    PostDto postDto= modelMapper.map(post,PostDto.class);
                    postDto.setUserDto(modelMapper.map(post.getUser(),UserDto.class));
                    postDto.setCategoryDto(modelMapper.map(post.getCategory(),CategoryDto.class));
                    return  postDto;

                })
                .toList();
    }

}
