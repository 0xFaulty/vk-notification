package com.defaulty.notivk.gui.service;

import com.vk.api.sdk.objects.docs.Doc;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.PhotoSizes;
import com.vk.api.sdk.objects.video.Video;
import com.vk.api.sdk.objects.wall.Graffiti;
import com.vk.api.sdk.objects.wall.Wallpost;
import com.vk.api.sdk.objects.wall.WallpostAttachment;
import com.vk.api.sdk.objects.wall.WallpostFull;

import java.util.List;

/**
 * The class {@code PostFullParser} обрабатывает контейнер постов {@code WallpostFull}
 * и находит в нём текст и ссылку на фото.
 */
public class PostFullParser {

    private String maxPhoto = null;
    private StringBuilder text = new StringBuilder();
    private WallpostFull postFull;

    public PostFullParser(WallpostFull wallpostFull) {
        this.postFull = wallpostFull;
        init();
    }

    public String getText() {
        return text.toString();
    }

    public String getPhotoLink() {
        return maxPhoto;
    }

    private void init() {
        text.append(postFull.getText()).append("\n");
        if (postFull.getCopyHistory() != null && postFull.getCopyHistory().size() > 0) {
            parseWallPost(postFull.getCopyHistory().get(0));
        } else {
            if (postFull.getAttachments() != null) attachmentsParse(postFull.getAttachments().get(0));
        }
    }

    private void parseWallPost(Wallpost post) {
        List<WallpostAttachment> wpaList = post.getAttachments();
        if (wpaList != null && wpaList.size() > 0) {
            attachmentsParse(wpaList.get(0));
        }
    }

    private void attachmentsParse(WallpostAttachment wpa) {

        switch (wpa.getType()) {
            case PHOTO:
                maxPhoto = getMaxPreview(wpa.getPhoto());
                text.append(wpa.getPhoto().getText());
                break;
            case POSTED_PHOTO:
                break;
            case AUDIO:
                text.append(wpa.getAudio().getArtist()).append(" - ").append(wpa.getAudio().getTitle());
                break;
            case VIDEO:
                maxPhoto = getMaxPreview(wpa.getVideo());
                text.append(wpa.getVideo().getTitle());
                break;
            case DOC:
                maxPhoto = getMaxPreview(wpa.getDoc());
                text.append(wpa.getDoc().getTitle());
                break;
            case LINK:
                maxPhoto = getMaxPreview(wpa.getLink().getPhoto());
                break;
            case GRAFFITI:
                maxPhoto = getMaxPreview(wpa.getGraffiti());
                break;
            case NOTE:
                break;
            case APP:
                break;
            case POLL:
                text.append(wpa.getPoll().getQuestion());
                break;
            case PAGE:
                break;
            case ALBUM:
                maxPhoto = getMaxPreview(wpa.getAlbum().getThumb());
                break;
            case PHOTOS_LIST:
                break;
            case MARKET_MARKET_ALBUM:
                break;
            case MARKET:
                break;
        }
    }

    private String getMaxPreview(Photo photo) {
        String str = null;
        if (photo != null) {
            if (photo.getPhoto75() != null) str = photo.getPhoto75();
            if (photo.getPhoto130() != null) str = photo.getPhoto130();
            if (photo.getPhoto604() != null) str = photo.getPhoto604();
            if (photo.getPhoto807() != null) str = photo.getPhoto807();
            if (photo.getPhoto1280() != null) str = photo.getPhoto1280();
            if (photo.getPhoto2560() != null) str = photo.getPhoto2560();
        }
        return str;
    }

    private String getMaxPreview(Video video) {
        String str = null;
        if (video != null) {
            if (video.getPhoto130() != null) str = video.getPhoto130();
            if (video.getPhoto320() != null) str = video.getPhoto320();
            if (video.getPhoto800() != null) str = video.getPhoto800();
        }
        return str;
    }

    private String getMaxPreview(Graffiti graffiti) {
        String str = null;
        if (graffiti != null) {
            if (graffiti.getPhoto200() != null) str = graffiti.getPhoto200();
            if (graffiti.getPhoto586() != null) str = graffiti.getPhoto586();
        }
        return str;
    }

    private String getMaxPreview(Doc doc) {
        String str = null;
        if (doc != null) {
            if (doc.getPreview().getPhoto().getSizes() != null) {
                List<PhotoSizes> sizes = doc.getPreview().getPhoto().getSizes();
                for (PhotoSizes size : sizes) {
                    str = size.getSrc();
                }
            }
        }
        return str;
    }
}
