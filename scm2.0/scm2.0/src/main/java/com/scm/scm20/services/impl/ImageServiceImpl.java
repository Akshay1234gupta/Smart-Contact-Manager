package com.scm.scm20.services.impl;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.scm.scm20.helper.AppConstants;
import com.scm.scm20.services.ImageService;


@Service
public class ImageServiceImpl implements ImageService {


    private Cloudinary cloudinary;

    public ImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }



    @Override
    public String uploadImage(MultipartFile contactImage,String filename) {
       
            // code likhna h jo image ko upload kar rha ho



            try {   
            byte[] data=new byte[contactImage.getInputStream().available()];

            contactImage.getInputStream().read(data);
            cloudinary.uploader().upload(data, ObjectUtils.asMap(
                "public_id",filename
            ));
            //return kar rha hoga ;url
            //return :image uploaded successfully
            return this.getUrlFromPublicid(filename);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    @Override
    public String getUrlFromPublicid(String publicid) {

        return cloudinary
        .url()
        .transformation(
            new Transformation<>()
            .width(AppConstants.CONTACT_IMAGE_WIDTH)
            .height(AppConstants.CONTACT_IMAGE_HEIGHT)
            .crop(AppConstants.CONTACT_IMAGE_CROP)
        )
        .generate(publicid);
    }

  
}
