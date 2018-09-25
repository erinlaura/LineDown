from sqlalchemy import *
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relation, sessionmaker
from random import randint
from datetime import datetime
import calendar
from sys import argv

PlaceID = argv[1]

Base = declarative_base()

class BaseDataSets(Base):
    __tablename__ = 'BaseDataSets'

    ID = Column(String(255), nullable=False, primary_key=True)
    PlaceID = Column(String(255), nullable=False)
    DayOfWeek = Column(String(3), nullable=False)
    TimeOfDay = Column(Integer, nullable=False)
    WaitTime = Column(Float(64), nullable=False)
    AvgPeopleNearby = Column(Float(64), nullable=False)
    DaysTrackedSince = Column(Integer, nullable=False)

    def __init__ (self, ID=None, PlaceID=None, DayOfWeek=None, TimeOfDay=None, WaitTime=None, AvgPeopleNearby=None, DaysTrackedSince=None):
        self.ID = ID
        self.PlaceID = PlaceID
        self.DayOfWeek = DayOfWeek
        self.TimeOfDay = TimeOfDay
        self.WaitTime = WaitTime
        self.AvgPeopleNearby = AvgPeopleNearby
        self.DaysTrackedSince = DaysTrackedSince

class UserInput(Base):
    __tablename__ = 'UserInput'
    
    ID = Column(String(255), nullable=False, primary_key=True)
    PlaceID = Column(String(255), nullable=False)
    WaitTime = Column(Integer, nullable=False)
    TimeInput = Column(Integer, nullable=False)
    
    def __init__ (self, ID=None, PlaceID=None, WaitTime=None, TimeInput=None):
        self.ID = ID
        self.PlaceID = PlaceID
        self.WaitTime = WaitTime
        self.TimeInput = TimeInput

class UserLocations(Base):
    __tablename__ = 'UserLocations'
    
    ID = Column(String(255), nullable=False, primary_key=True)
    PeopleNearby = Column(Integer, nullable=False)
    
    def __init__ (self, ID=None, PeopleNearby=None):
        self.ID = ID
        self.PeopleNearby = PeopleNearby

class WaitTimes(Base):
    __tablename__ = 'WaitTimes'
    
    ID = Column(String(255), nullable=False, primary_key=True)
    CurrentWaitTime = Column(Float(64), nullable=False)
    LastUpdated = Column(Integer, nullable=False)
    
    def __init__ (self, ID=None, CurrentWaitTime=None, LastUpdated=None):
        self.ID = ID
        self.CurrentWaitTime = CurrentWaitTime
        self.LastUpdated = LastUpdated
    

engine = create_engine('mysql://root:LineDown5895@104.197.169.245/LineDown')
Base.metadata.create_all(engine)
Session = sessionmaker(bind=engine)
session = Session()

daysOfWeek = ["Sun","Mon","Tue","Wed","Thu","Fri","Sat"]
typicalWaitTime = [9999,9999,9999,9999,9999,9999,9999,9999,9999,10,5,12,20,17,10,5,11,10,6,9999,9999,9999,9999,9999]
typicalPeopleNearby = [9999,9999,9999,9999,9999,9999,9999,9999,9999,8,4,9,16,13,8,4,9,8,4,9999,9999,9999,9999,9999]


d = datetime.utcnow()
unixtime = calendar.timegm(d.utctimetuple())
unixtimeminutes = unixtime/60

for i in range(7):
    for j in range(24):
        ID = PlaceID + daysOfWeek[i] + str(j)
        DayOfWeek = daysOfWeek[i]
        TimeOfDay = j
        WaitTime = (float(randint(1,100))/100)*float(typicalWaitTime[j])
        AvgPeopleNearby = 1
        DaysTrackedSince = randint(17000,17100)
        
        try:
            session.add(BaseDataSets(ID,PlaceID,DayOfWeek,TimeOfDay,WaitTime,AvgPeopleNearby,DaysTrackedSince))
            session.commit()
        except:
            print "Error with BaseDataSet"
            session.rollback() 
            
for i in range(5):
    TimeInput = randint(unixtimeminutes-100, unixtimeminutes)
    waitTime = randint(1,30)
    ID = PlaceID + str(TimeInput) + str(waitTime)
    
    try:
        session.add(UserInput(ID,PlaceID,waitTime,TimeInput))
        session.commit()
    except:
        print "Error with UserInput"
        session.rollback()

PeopleNearby = 1
try:
    session.add(UserLocations(PlaceID,PeopleNearby))
    session.commit()
except:
    print "Error with UserLocations"
    session.rollback()

CurrentWaitTime = randint(1,25)
LastUpdated = randint(unixtimeminutes-10, unixtimeminutes)
try:
    session.add(WaitTimes(PlaceID,CurrentWaitTime,LastUpdated))
    session.commit()
except:
    print "Error with WaitTimes"
    session.rollback()
    
session.close()
    
