package com.xeno.game.network.common;

public class PlayerState {
		
        public int Direction;

        public float X;
        public float Y;

        public boolean Moving;

        public PlayerState()
        { }

        public PlayerState(int direction, float x, float y)
        {
            Direction = direction;
            X = x;
            Y = y;
        }

        public PlayerState(int direction, float x, float y, boolean moving, boolean running, int attack)
        {
            Direction = direction;
            X = x;
            Y = y;
            Moving = moving;
        }

        public PlayerState(PlayerState copy)
        {
            Direction = copy.Direction;
            X = copy.X;
            Y = copy.Y;
            Moving = copy.Moving;
        }

//        public void WriteToPacket(NetOutgoingMessage msg)
//        {
//            msg.Write((byte)Direction);
//            msg.Write(X);
//            msg.Write(Y);
//            msg.Write(Moving);
//            msg.Write(Running);
//            msg.Write(Attack);
//        }
//
//        public void ReadFromPacket(NetIncomingMessage msg)
//        {
//            Direction = (Direction)msg.ReadByte();
//            X = msg.ReadFloat();
//            Y = msg.ReadFloat();
//            Moving = msg.ReadBoolean();
//            Running = msg.ReadBoolean();
//            Attack = msg.ReadInt32();
//        }
        
}
