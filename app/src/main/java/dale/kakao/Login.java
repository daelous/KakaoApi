package dale.kakao;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.AuthService;
import com.kakao.auth.AuthType;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.auth.network.response.AccessTokenInfoResponse;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

/**
 * Created by KimDaeIl on 16. 8. 21..
 */
public class Login {

    private final String TAG=getClass().getSimpleName();

    private Activity mActivity;
    private SessionCallback mSessionCallback;
    private long mUserToken = 0;
    private long mUserTokenvalidDate = 0;

    public Login(Activity activity) {
        mActivity = activity;

    }


    public void loginKakaoUesr() {
        mSessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(mSessionCallback);

        if (!Session.getCurrentSession().checkAndImplicitOpen())
            if (mActivity != null)
                Session.getCurrentSession().open(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN, mActivity);
    }

    public void removeCallback() {
        Session.getCurrentSession().removeCallback(mSessionCallback);

    }

    private void requestMe() {

        //내 프로필 정보 요청
        UserManagement.requestMe(new MeResponseCallback() {

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                //정보 가져오는걸 실패했을 때

                int errorCode = errorResult.getErrorCode();

                if (errorCode == ErrorCode.CLIENT_ERROR_CODE.getErrorCode()) {
                    Toast.makeText(mActivity.getApplicationContext(), "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onNotSignedUp() {
                //가입 정보가 없을 때 (비회원)

            }

            @Override
            public void onSuccess(UserProfile result) {
                //정보 가져왔을 때

                Log.d(TAG, result.getNickname() + "");
                Log.d(TAG, result.getThumbnailImagePath() + "");

                requestUserToken();

            }
        });

    }

    public void requestUserToken() {
        AuthService.requestAccessTokenInfo(new ApiResponseCallback<AccessTokenInfoResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                super.onFailure(errorResult);
            }

            @Override
            public void onSuccess(AccessTokenInfoResponse result) {
                mUserToken = result.getUserId();
                mUserTokenvalidDate = result.getExpiresInMillis();

            }

            @Override
            public void onFailureForUiThread(ErrorResult errorResult) {
                super.onFailureForUiThread(errorResult);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {

            }

            @Override
            public void onNotSignedUp() {

            }
        });
    }

    public long getUserToken() {
        return mUserToken;

    }

    public void setUserToken(long userToken) {
        mUserToken = userToken;

    }

    public long getUserTokenvalidDate() {
        return mUserTokenvalidDate;

    }

    public void setUserTokenvalidDate(long userTokenvalidDate) {
        mUserTokenvalidDate = userTokenvalidDate;

    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            //세션이 연결되었을 때
            requestMe();

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            // 세션 연결이 실패했을때

            if (exception != null) {
                Logger.e(exception);


            }
        }
    }

}
