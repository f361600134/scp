package com.anjiu.qlbs.backup;

public class ScpResult {

	@Override
	public String toString() {
		return "ScpResult [resultCode=" + resultCode + ", resultInfo=" + resultInfo + "]";
	}

	private Integer resultCode;
	private String resultInfo;

	public ScpResult() {
	}

	public static ScpResult create(Integer resultCode, String resultInfo) {
		ScpResult scpResult = new ScpResult();
		scpResult.setResultCode(resultCode);
		scpResult.setResultInfo(resultInfo);
		return scpResult;
	}

	public static ScpResult create() {
		ScpResult scpResult = new ScpResult();
		scpResult.setResultCode(-1);
		scpResult.setResultInfo("error");
		return scpResult;
	}

	/**
	 * 0, null表示成功
	 * 
	 * @return
	 * @return boolean
	 * @date 2018年8月27日下午10:19:48
	 */
	public boolean isSuccess() {
		// if (resultCode != null) {
		// return resultCode == 0;
		// }
		// return true;
		return resultCode == null ? true : resultCode == 0;
	}

	public Integer getResultCode() {
		return resultCode;
	}

	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultInfo() {
		return resultInfo;
	}

	public void setResultInfo(String resultInfo) {
		this.resultInfo = resultInfo;
	}

}
