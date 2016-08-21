package dale.kakao;

import android.content.Context;

import com.kakao.kakaolink.AppActionBuilder;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

/**
 * Created by KimDaeIl on 16. 8. 21..
 */
public class Link {

    private String mPostIdStr = "";
    private int mImageWidth = 0;
    private int mImageHeight = 0;
    private String mTextStr = "";
    private String mImagePathStr = "";


    private Context mContext;

    private KakaoTalkLinkMessageBuilder mKakaoTalkLinkMessageBuilder;
    private AppActionBuilder mAppActionBuilder;
    private KakaoLink mKakaoLink;

    public Link(Context context) throws KakaoParameterException {
        mContext = context;

        mKakaoLink = KakaoLink.getKakaoLink(mContext);
        mKakaoTalkLinkMessageBuilder = mKakaoLink.createKakaoTalkLinkMessageBuilder();

    }

    public void sendLink(String postId) throws KakaoParameterException {

        if (mPostIdStr.equals(""))
            initAppLink(postId);

        if (!mPostIdStr.equals(postId))
            setAppLinkUrl(postId);

        mKakaoLink.sendMessage(mKakaoTalkLinkMessageBuilder, mContext);

    }

    private void initAppLink(String postId) throws KakaoParameterException {
        mKakaoTalkLinkMessageBuilder.addText(mTextStr);
        mKakaoTalkLinkMessageBuilder.addImage(mImagePathStr, mImageWidth, mImageHeight);

        mAppActionBuilder = new AppActionBuilder();

        setAppLinkUrl(postId);

    }

    private void setAppLinkUrl(String postId) throws KakaoParameterException {
        mAppActionBuilder.setUrl(postId);
        mKakaoTalkLinkMessageBuilder.addAppLink("앱으로 이동,", mAppActionBuilder.build());

        mPostIdStr = postId;

    }
}
