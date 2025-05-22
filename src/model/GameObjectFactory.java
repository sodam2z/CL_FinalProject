package model;

public class GameObjectFactory {
    public static GameObject createFromSymbol(char ch) {
        switch (ch) {
            case '@': return new Hero();
            case 'S': return new Weapon("Stick", 1, 'S');
            case 'W': return new Weapon("Weak Sword", 2, 'W');
            case 'X': return new Weapon("Strong Sword", 3, 'X');
            case 'm': return new Potion("Minor Flask", 6, 'm');
            case 'B': return new Potion("Big Flask", 12, 'B');
            case 'G': return new Monster("Goblin", 3, 1, 'G');
            case 'O': return new Monster("Orc", 8, 3, 'O');
            case 'T': return new Monster("Troll", 15, 4, 'T');
            case 'D': return new Door("rooms/room2.csv"); // 문은 실제 주소를 나중에 로딩하도록 수정 가능
            case '*': return new Key();
            case ' ': return null;
            default:  return null;
        }
    }
}