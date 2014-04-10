package com.gt.brewmasters.structures;

public class RecipeInfo {
	  private int mID = -1;
	  private String mDate = null;
	  
	  private String mName = null;
	  private String mStyle = null;
	  private String mDescription = null;
	  private String mIDNumber = null;
	  private String mGender = null;
	  private String mRemarks = null;
	  
	  private String mPhoto = null;
	  private String mDoc1 = null;
	  private String mDoc2 = null;
	  private String mDesc1 = null;
	  private String mDesc2 = null;
	  
	  private String mFpRIM = null;
	  private String mFpLIM = null;
	  private String mFpRRL = null;
	  private String mFpLRL = null;
	  private String mFpBT = null;
	  
	  private String mIrisLeft = null;
	  private String mIrisRight = null;
	  
	  private String mState = null;
	  private String mErrMsg = null;
	    
//	  public DemoInfo()
//	  {
//	  }

//	  public DemoInfo(String strUUID, String strDate,
//			  		  String strName, String strAddress,
//			  		  String strDOB,  String strIDNumber,
//			  		  String strGender, String strRemarks,
//			  		  String strPhoto, String strDoc1, String strDoc2,
//			  		  String strDes1, String strDes2,
//			  		  String strFpRIM, String strFpLIM,
//			  	 	  String strFpRRL, String strFpLRL, 
//			  	  	  String strFpBT, String strIrisLeft,
//			  	  	  String strIrisRight,
//			  		  String strState, String strErrMsg)
//	  {
//		  mUUID = strUUID;
//		  mDate = strDate;
//		  
//		  mName = strName;
//		  mAddress = strAddress;
//		  mDOB = strDOB;
//		  mIDNumber = strIDNumber;
//		  mGender = strGender;
//		  mRemarks = strRemarks;
//		  
//		  mPhoto = strPhoto;
//		  mDoc1 = strDoc1;
//		  mDoc2 = strDoc2;
//		  mDesc1 = strDes1;
//		  mDesc2 = strDes2;	  
//		  
//		  mFpRIM = strFpRIM;
//		  mFpLIM = strFpLIM;
//		  mFpRRL = strFpRRL;
//		  mFpLRL = strFpLRL;
//		  mFpBT = strFpBT;
//
//		  mIrisLeft = strIrisRight;
//		  mIrisRight = strIrisRight;
//		  
//		  mState = strState;
//		  mErrMsg = strErrMsg;
//	  }

	  //ID
	  public int getID()
	  {
	    return mID;
	  }
	  
	  public void setID(int paramInt)
	  {
	    mID = paramInt;
	  }
	  
	  //UUID
//	  public String getUUID()
//	  {
//	    //return mUUID;
//	  }
	  
	  public void setUUID(String paramString)
	  {
		  //mUUID = paramString;
	  }
	  
	  //mDate
	  public String getDate()
	  {
	    return mDate;
	  }
	  
	  public void setDate(String paramString)
	  {
		  mDate = paramString;
	  }  
	  
	  //Name
	  public String getName()
	  {
	    return mName;
	  }
	  
	  public void setName(String paramString)
	  {
		  mName = paramString;
	  }
	  
	  //Address
//	  public String getAddress()
//	  {
//	    //return mAddress;
//	  }
	  
	  public void setAddress(String paramString)
	  {
		 // mAddress = paramString;
	  }
	  
	  //mDOB
//	  public String getDOB()
//	  {
//	    //return mDOB;
//	  }
	  
	  public void setDOB(String paramString)
	  {
		//  mDOB = paramString;
	  }
	  
	  //IDNumber
	  public String getIDNumber()
	  {
	    return mIDNumber;
	  }
	  
	  public void setIDNumber(String param)
	  {
		  mIDNumber = param;
	  }
	  
	  //mGender
	  public String getGender()
	  {
	    return mGender;
	  }
	  
	  public void setGender(String paramString)
	  {
		  mGender = paramString;
	  }

	  //mRemarks
	  public String getRemarks()
	  {
	    return mRemarks;
	  }
	  
	  public void setRemarks(String paramString)
	  {
		  mRemarks = paramString;
	  }
	  
	  //photo
	  public String getPhoto()
	  {
	    return mPhoto;
	  }
	  
	  public void setPhoto(String paramString)
	  {
		  mPhoto = paramString;
	  }  
	  
	  //Doc1
	  public String getDoc1()
	  {
	    return mDoc1;
	  }
	  
	  public void setDoc1(String paramString)
	  {
		  mDoc1 = paramString;
	  } 
	  
	  //Doc2
	  public String getDoc2()
	  {
	    return mDoc2;
	  }
	  
	  public void setDoc2(String paramString)
	  {
		  mDoc2 = paramString;
	  }
	  
	  //Description_1
	  public String getDesc1()
	  {
	    return mDesc1;
	  }
	  
	  public void setDesc1(String paramString)
	  {
		  mDesc1 = paramString;
	  }  
	  
	  //Description_2
	  public String getDesc2()
	  {
	    return mDesc2;
	  }
	  
	  public void setDesc2(String paramString)
	  {
		  mDesc2 = paramString;
	  }
	  
	  public String getFpRIM()
	  {
	    return mFpRIM;
	  }
	  
	  public void setFpRIM(String paramString)
	  {
		  mFpRIM = paramString;
	  } 
	  
	  public String getFpLIM()
	  {
	    return mFpLIM;
	  }
	  
	  public void setFpLIM(String paramString)
	  {
		  mFpLIM = paramString;
	  } 

	  public String getFpRRL()
	  {
	    return mFpRRL;
	  }
	  
	  public void setFpRRL(String paramString)
	  {
		  mFpRRL = paramString;
	  }
	  
	  public String getFpLRL()
	  {
	    return mFpRIM;
	  }
	  
	  public void setFpLRL(String paramString)
	  {
		  mFpLRL = paramString;
	  } 

	  public String getFpBT()
	  {
	    return mFpBT;
	  }
	  
	  public void setFpBT(String paramString)
	  {
		  mFpBT = paramString;
	  } 

	  public String getIrisLeft()
	  {
	    return mIrisLeft;
	  }
	  
	  public void setIrisLeft(String paramString)
	  {
		  mIrisLeft = paramString;
	  } 
	  
	  public String getIrisRight()
	  {
	    return mIrisRight;
	  }
	  
	  public void setIrisRight(String paramString)
	  {
		  mIrisRight = paramString;
	  } 
	  
	  //State
	  public String getState()
	  {
	    return mState;
	  }
	  
	  public void setState(String paramString)
	  {
		  mState = paramString;
	  }  
	  
	  //ErrMsg
	  public String getErrMsg()
	  {
	    return mErrMsg;
	  }
	  
	  public void setErrMsg(String paramString)
	  {
		  mErrMsg = paramString;
	  } 
	//}

}
