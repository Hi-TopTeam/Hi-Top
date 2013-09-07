package ui.activity.community;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.kobjects.base64.Base64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import com.DreamTeam.HiTop.R;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import ui.activity.ActivityOfAF4Ad;
import ui.viewModel.ModelErrorInfo;
import ui.viewModel.ViewModel;
import webservice.HiTopWebPara;
import webservice.WebServiceDelegate;
import webservice.WebServiceUtils;

public class BrowseFriendshareActivity extends ActivityOfAF4Ad implements
		WebServiceDelegate {
	private LinearLayout myGallery;
	private WebServiceUtils webService;
	private String friendUsername;
	private ProgressDialog mpDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friendshare);
		Bundle bundle = getIntent().getExtras();
		friendUsername = bundle.getString("friendusername");
	}

	@Override
	protected void initControlsAndRegEvent() {
		myGallery = (LinearLayout) findViewById(R.id.mygallery);
		//实例化mpDialog
		mpDialog = new ProgressDialog(this);
		mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mpDialog.setIndeterminate(true);
		mpDialog.setTitle("数据加载中");
		mpDialog.show();
		webService = new WebServiceUtils(HiTopWebPara.CM_NAMESPACE,
				HiTopWebPara.CM_URL, this);
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("username", friendUsername);
		webService.callWebService("getShareDataByUser", args, List.class);

	}

	@Override
	protected ViewModel initModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void upDateView(ViewModel aVM) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processViewModelErrorMsg(List<ModelErrorInfo> errsOfVM,
			String errMsg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleException(Object ex) {
		mpDialog.dismiss();
		Toast toast = Toast.makeText(this, "更新失败,请检查网络连接", Toast.LENGTH_SHORT);
		toast.show();

	}

	@Override
	public void handleResultOfWebService(String methodName, Object result) {
		List<SoapObject> list = new ArrayList<SoapObject>();
		List<byte[]> list_b = new ArrayList<byte[]>();
		if (result != null) {
			if (result instanceof Vector) {
				Vector vector = (Vector) result;
				for (int i = 0; i < vector.size(); i++)
					list.add((SoapObject) vector.get(i));
			} else {
				SoapObject soapObject = (SoapObject) result;
				list.add(soapObject);
			}
			Iterator it = list.iterator();
			while (it.hasNext()) {
				SoapObject sObject = (SoapObject) it.next();
				String tmp = ((SoapPrimitive) sObject.getProperty("img"))
						.toString();
				byte[] retByte = Base64.decode(tmp);
				list_b.add(retByte);
			}
			for (int i = 0; i < list_b.size(); i++) {
				myGallery.addView(insertImage(list_b.get(i)));
			}
		}else{
			setContentView(R.layout.activity_lazy);
		}
		mpDialog.dismiss();
	}

	public static Bitmap BytesToBitmap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	public static Drawable resizeImage(Bitmap bitmap, int w, int h) {
		// load the origial Bitmap
		Bitmap BitmapOrg = bitmap;
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;
		// calculate the scale
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the Bitmap
		matrix.postScale(scaleWidth, scaleHeight);
		// if you want to rotate the Bitmap
		// matrix.postRotate(45);
		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
				height, matrix, true);
		// make a Drawable from Bitmap to allow to set the Bitmap
		// to the ImageView, ImageButton or what ever
		return new BitmapDrawable(resizedBitmap);
	}

	private View insertImage(byte[] img) {
		LinearLayout layout = new LinearLayout(getApplicationContext());
		layout.setLayoutParams(new LayoutParams(500, 833));
		layout.setGravity(Gravity.CENTER);
		ImageView imageView = new ImageView(getApplicationContext());
		imageView.setLayoutParams(new LayoutParams(460, 767));
		imageView.setImageBitmap(BytesToBitmap(img));
		layout.addView(imageView);
		return layout;
	}

}
