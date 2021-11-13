import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class game_manager {

    private static List roomList; // 방의 리스트
    private static AtomicInteger atomicInteger;

    static {
        roomList = new ArrayList();
        atomicInteger = new AtomicInteger();
    }

    public game_manager() {

    }

    /**
     * 빈 룸을 생성
     * @return GameRoom
     */
    public static game_room createRoom() { // 룸을 새로 생성(빈 방)
        int roomId = atomicInteger.incrementAndGet();// room id 채번
        game_room room = new game_room(roomId);
        roomList.add(room);
        System.out.println("Room Created!");
        return room;
    }

    /**
     * 방을 생성함과 동시에 방장을 만들어줌
     * @param owner 방장
     * @return GameRoom
     */
    public static game_room createRoom(game_user owner) { // 유저가 방을 생성할 때 사용(유저가 방장으로 들어감)
        int roomId = atomicInteger.incrementAndGet();// room id 채번

        game_room room = new game_room(roomId);
        room.enterUser(owner);
        room.setOwner(owner);

        roomList.add(room);
        System.out.println("Room Created!");
        return room;
    }

    /**
     * 유저 리스트로 방을 생성
     * @param users 입장시킬 유저 리스트
     * @return GameRoom
     */
    public static game_room createRoom(List users) {
        int roomId = atomicInteger.incrementAndGet();// room id 채번

        game_room room = new game_room(roomId);
        room.enterUser(users);

        roomList.add(room);
        System.out.println("Room Created!");
        return room;
    }

    public static game_room getRoom(game_room gameRoom){

        int idx = roomList.indexOf(gameRoom);

        if(idx > 0){
            return (game_room) roomList.get(idx);
        }
        else{
            return null;
        }
    }

    /**
     * 전달받은 룸을 제거
     * @param room 제거할 룸
     */
    public static void removeRoom(game_room room) {
        room.close();
        roomList.remove(room); // 전달받은 룸을 제거한다.
        System.out.println("Room Deleted!");
    }

    /**
     * 방의 현재 크기를 리턴
     * @return 현재 size
     */
    public static int roomCount() {
        return roomList.size();
    }
}