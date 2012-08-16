package com.aigilas.items;
    
    public class ItemFactory
    {
        private static final int itemGrowth = 3;
        private static int itemType;

        public static GenericItem CreateRandomPlain(Point2 location,boolean onFloor) 
        {
            if(location == null)
            {
                location = new Point2(-100,-100);
            }

            itemType = SelectRandomType();
            return
                (GenericItem)
                EntityManager.addObject(new GenericItem(new Stats(RNG.Rand.nextInt(itemGrowth), RNG.Rand.nextInt(itemGrowth), RNG.Rand.nextInt(itemGrowth), 0, 0, 0, 0, 0, 0, 0),
                                                                ItemSuffix.NULL, ItemPrefix.NULL, itemType,
                                                                location,onFloor));
        }
        public static GenericItem CreateRandomPlain(Point2 location) 
        {
            return CreateRandomPlain(location, true);
        }
        public static GenericItem CreateRandomMagic()
        {
            return null;
            //return new IItem(new Stats(1,1,1,1,1,1,0,1,0,0,0),)}
        }
        private static int SelectRandomType()
        {
            return ItemName.Values[(RNG.Next(1, ItemName.Values.length))];
        }
}