package infrastructure.socialmedia.pinterest;

public class PinDAO {

	public BoardDAO getBoardFromAPI(){
		return new BoardDAO();
	}

	public boolean login(PinterestUserDTO dto) {
		return false;
	}
	
	public BoardDTO getBoardDetails(Object o){
		return new BoardDTO();
	}
}