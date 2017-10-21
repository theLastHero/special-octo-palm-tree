package PlayerWarpGUI;

public class Response {

	private String errorMsg;
	private boolean responseBool;

	public Response(boolean showError, String errorMsg) {

		this.responseBool = showError;
		this.errorMsg = errorMsg;

	}

	public void setResponseBool(boolean responseBool) {
		this.responseBool = responseBool;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public boolean getResponseBool() {
		return responseBool;
	}

	public void setresponseBool(boolean responseBool) {
		this.responseBool = responseBool;
	}

}
