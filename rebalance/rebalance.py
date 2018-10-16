
# coding: utf-8

# In[5]:


import json
import requests
import datetime
import pandas as pd
import urllib.request
import codecs


# In[6]:

STATIC_CONSTANTS_JAVA_FILE = "../index-data/src//main/java/com/frobro/bcindex/web/constants/StaticValues.java"

list_indexes = ['10' , '20' , '40' , 'Total' , 'ETH' , 'Currency' , 'Platform' , 'Application']
weights_API_list = ['TEN' , 'TWENTY' , 'FORTY' , 'TOTAL' , 'ETHEREUM' , 'CURRENCY' , 'PLATFORM' , 'APPLICATION']
old_month = 'october'            #change these every rebalance
new_month = 'november'          #change these every rebalance


# In[7]:


#change file path below! Three times!
#read in inputs and declare variables
indexes = dict.fromkeys(list_indexes)
#change below to point to prod business_rules
old_constants = pd.read_csv('../index-data/src/main/resources/business_rules/All_'+old_month+'.csv' , index_col=[0])
new_values = old_constants * 1
new_values['Mkt Cap'] = old_constants['USD'] * 0
new_values['Adj Mkt Cap'] = old_constants['USD'] * 0
for k in indexes:
    if (k == 'Currency' or k == 'Platform' or k == 'Application'):
        indexes[k] = pd.DataFrame()
        #change below to point to prod business_rules
        indexes[k] = pd.read_csv('../index-data/src/main/resources/business_rules/'+k+'_'+new_month+'_rebal.csv' , index_col=[0] , names=[k,'Adj Float'])
    else:
        indexes[k] = pd.DataFrame()
        #change below to point to prod business_rules
        indexes[k] = pd.read_csv('../index-data/src/main/resources/business_rules/'+k+'_'+new_month+'_rebal.csv' , index_col=[0] , names=[k,'Float' , 'Adj Float'])


# In[8]:


url = "https://api.nomics.com/v1/prices?key=8ab88c64570680aeb728a3109e69dd96"
#page = (urllib.request.urlopen(url).read())
# response = (urllib.request.urlopen(url).read())
# response = response.decode('utf-8')
response = requests.get(url)
d = response.json()
all_prices = pd.DataFrame(d)
all_prices = all_prices.set_index('currency')
all_prices['Price'] = pd.to_numeric(all_prices['price'])


# In[9]:


def daily_price_historical(symbol,base):
    a = all_prices.loc[symbol]['Price']
    return a


# In[10]:


#get BTC price
bitcoin = daily_price_historical('BTC','USD')


# In[12]:


#calculate new index values, only do one for sectors (just one weighting scheme)
for k in indexes:
    if (k == 'Currency' or k == 'Platform' or k == 'Application'):
        for t in indexes[k].index:
            try:
                df = daily_price_historical(t,'USD')
                indexes[k].loc[t,('Price')] = df
            except:
                print (t , 'no price!')
        indexes[k]['Mkt Cap'] = indexes[k]['Price']*indexes[k]['Adj Float']
        indexes[k]['Weight'] = indexes[k]['Mkt Cap'] / indexes[k]['Mkt Cap'].sum()
        sum_weight = indexes[k]['Weight'].sum()
        #print k, sum_weight
        new_values.loc[k,('Mkt Cap')] = indexes[k]['Mkt Cap'].sum()
        new_values.loc[k,('USD')] = indexes[k]['Mkt Cap'].sum() / new_values.loc[k,('Divisor')]
        new_values.loc[k,('BTC')] = new_values.loc[k,('USD')] / bitcoin
        if (indexes[k]['Price'] < 0.001).any() == True:
            print (k, 'missing ticker!')
    else:
        for t in indexes[k].index:
            try:
                df = daily_price_historical(t,'USD')
                indexes[k].loc[t,('Price')] = df
            except:
                print (t , 'no price!')
        indexes[k]['Mkt Cap'] = indexes[k]['Price']*indexes[k]['Float']
        indexes[k]['Weight'] = indexes[k]['Mkt Cap'] / indexes[k]['Mkt Cap'].sum()
        indexes[k]['Equal'] = 1.00 / (len(indexes[k]))
        indexes[k]['Adj Mkt Cap'] = indexes[k]['Price'] * indexes[k]['Adj Float']
        indexes[k]['Adj Weight'] = indexes[k]['Adj Mkt Cap'] / indexes[k]['Adj Mkt Cap'].sum()
        sum_weight = indexes[k]['Weight'].sum()
        sum_equal_weight = indexes[k]['Adj Weight'].sum()
        #print k, sum_weight, sum_equal_weight
        new_values.loc[k,('Mkt Cap')] = indexes[k]['Mkt Cap'].sum()
        new_values.loc[k,('Adj Mkt Cap')] = indexes[k]['Adj Mkt Cap'].sum()
        new_values.loc[k,('USD')] = indexes[k]['Mkt Cap'].sum() / new_values.loc[k,('Divisor')]
        new_values.loc[k,('Adj USD')] = indexes[k]['Adj Mkt Cap'].sum() / new_values.loc[k,('Adj Divisor')]
        new_values.loc[k,('BTC')] = new_values.loc[k,('USD')] / bitcoin
        new_values.loc[k,('Adj BTC')] = new_values.loc[k,('Adj USD')] / bitcoin
        if (indexes[k]['Price'] < 0.001).any() == True:
            print (k, 'missing ticker!')


# In[13]:


#change file path below! four times!
#read in input and declare variables
indexes , indexes_old = dict.fromkeys(list_indexes) , dict.fromkeys(list_indexes)
indexes_clean = dict.fromkeys(list_indexes)
indexes_new , old_weights = dict.fromkeys(list_indexes) , dict.fromkeys(list_indexes)
new_weights , new_weights_clean = dict.fromkeys(list_indexes) , dict.fromkeys(list_indexes)
old_weights_clean , turnover = dict.fromkeys(list_indexes) , dict.fromkeys(list_indexes)
indexes_float_change = dict.fromkeys(list_indexes)
old_constants = new_values
new_constants = old_constants * 0
new_constants = new_constants.drop(['Mkt Cap' , 'Adj Mkt Cap'],axis=1)

fields = [0 , 4]
fields_sector = [0, 12]

for k in indexes:
    indexes_float_change[k] = pd.DataFrame()
    if (k == 'Currency' or k == 'Platform' or k == 'Application'):
        indexes[k] = pd.DataFrame()
        #change to point to prod business_rules dir!
        indexes[k] = pd.read_csv('../index-data/src/main/resources/business_rules/'+k+'_pre_'+new_month+'.csv' , index_col=[0])
        indexes_old[k] = pd.read_csv('../index-data/src/main/resources/business_rules/'+k+'_'+old_month+'_rebal.csv' , index_col=[0] , names=[k,'Adj Float'])
    else:
        indexes[k] = pd.DataFrame()
        #change to point to prod business_rules dir!
        indexes[k] = pd.read_csv('../index-data/src/main/resources/business_rules/'+k+'_pre_'+new_month+'.csv' , index_col=[0])
        indexes_old[k] = pd.read_csv('../index-data/src/main/resources/business_rules/'+k+'_'+old_month+'_rebal.csv' , index_col=[0] , names=[k,'Float','Adj Float'])
    indexes_new[k] = indexes[k]


# In[15]:


#main function to calculate new floats and divisors
for k in indexes:
    if (k == 'Currency' or k == 'Platform' or k == 'Application'):    #sectors are seperate due to capping
        for t in indexes[k].index:
            try:
                df = daily_price_historical(t,'USD')
                indexes[k].loc[t,('Price')] = df
            except:
                print (t , 'error')
        if (k == 'Platform' or k == 'Application'):
            weight_cap = 0.2500
        else:
            weight_cap = 2.0000
        indexes[k]['Mkt Cap'] = indexes[k]['Price']*indexes[k]['Float']
        indexes[k]['Weight'] = indexes[k]['Mkt Cap'] / indexes[k]['Mkt Cap'].sum()
        indexes[k]['Weight Cap'] = weight_cap
        indexes[k]['Weight1'] = indexes[k]['Weight'] * 1
        #start looping through to handle the 25% capping rate
        for t in indexes[k].index:
            indexes[k].loc[t,('Weight1')] = min(indexes[k].loc[t,('Weight')] , indexes[k].loc[t,('Weight Cap')])
        sum1 = indexes[k]['Weight1'].sum()
        resid1 = 1.000 - sum1
        count1 = (sum(f > weight_cap for f in indexes[k]['Weight1'])) * weight_cap
        to_up1 = sum1 - count1
        mult1 = 1.000 + (resid1/to_up1)
        #print mult1 , to_up1, count1, resid1, sum1
        for t in indexes[k].index:
            if indexes[k].loc[t,('Weight1')] < (weight_cap - 0.0001):
                indexes[k].loc[t,('Weight_step2')] = indexes[k].loc[t,('Weight1')] * mult1
                indexes[k].loc[t,('Weight2')] = min(indexes[k].loc[t,('Weight_step2')] , indexes[k].loc[t,('Weight Cap')])
            else:
                indexes[k].loc[t,('Weight_step2')] = indexes[k].loc[t,('Weight1')]
                indexes[k].loc[t,('Weight2')] = indexes[k].loc[t,('Weight1')]
        sum2 = indexes[k]['Weight2'].sum()
        resid2 = 1.000 - sum2
        count2 = (sum(f >= weight_cap for f in indexes[k]['Weight2'])) * weight_cap
        to_up2 = sum2 - count2
        mult2 = 1.000 + (resid2/to_up2)
        #print mult2 , to_up2, count2, resid2, sum2
        for t in indexes[k].index:
            if indexes[k].loc[t,('Weight2')] < (weight_cap - 0.0001):
                indexes[k].loc[t,('Weight_step3')] = indexes[k].loc[t,('Weight2')] * mult2
                indexes[k].loc[t,('Weight3')] = min(indexes[k].loc[t,('Weight_step3')] , indexes[k].loc[t,('Weight Cap')])
            else:
                indexes[k].loc[t,('Weight_step3')] = indexes[k].loc[t,('Weight2')]
                indexes[k].loc[t,('Weight3')] = indexes[k].loc[t,('Weight2')]
        sum3 = indexes[k]['Weight3'].sum()
        resid3 = 1.000 - sum3
        count3 = (sum(f >= weight_cap for f in indexes[k]['Weight3'])) * weight_cap
        to_up3 = sum3 - count3
        mult3 = 1.000 + (resid3/to_up3)
        #print mult3 , to_up3, count3, resid3, sum3
        for t in indexes[k].index:
            if indexes[k].loc[t,('Weight3')] <= weight_cap:
                indexes[k].loc[t,('Weight_step4')] = indexes[k].loc[t,('Weight3')] * mult3
                indexes[k].loc[t,('Weight4')] = min(indexes[k].loc[t,('Weight_step4')] , indexes[k].loc[t,('Weight Cap')])
            else:
                indexes[k].loc[t,('Weight_step4')] = indexes[k].loc[t,('Weight3')]
                indexes[k].loc[t,('Weight4')] = indexes[k].loc[t,('Weight3')]
        #print indexes[k]
        sum4 = indexes[k]['Weight4'].sum()
        resid4 = 1.000 - sum4
        count4 = (sum(f >= weight_cap for f in indexes[k]['Weight4'])) * weight_cap
        to_up4 = sum4 - count4
        mult4 = 1.000 + (resid4/to_up4)
        #print k, mult4 , to_up4, count4, resid4, sum4
        #done iterating to compute proper adjusted weights
        indexes[k]['AWF'] = indexes[k]['Weight4'] / indexes[k]['Weight']
        indexes[k]['Adj Mkt Cap'] = indexes[k]['Mkt Cap'] * indexes[k]['AWF']
        indexes[k]['Adj Weight'] = indexes[k]['Adj Mkt Cap'] / indexes[k]['Adj Mkt Cap'].sum()
        indexes[k]['Adj Float'] = indexes[k]['Float'] * indexes[k]['AWF']
        sum_equal_weight = indexes[k]['Adj Weight'].sum()
        new_constants.loc[k,('Divisor')] = old_constants.loc[k,('Divisor')] *        (indexes[k]['Adj Mkt Cap'].sum() / old_constants.loc[k,('Mkt Cap')])
        new_constants.loc[k,('USD')] = indexes[k]['Adj Mkt Cap'].sum() / new_constants.loc[k,('Divisor')]
        new_constants.loc[k,('BTC')] = new_constants.loc[k,('USD')] / bitcoin
    else:
        for t in indexes[k].index:
            #print t
            try:
                df = daily_price_historical(t,'USD')
                indexes[k].loc[t,('Price')] = df
            except:
                print (t , 'error')
        indexes[k]['Mkt Cap'] = indexes[k]['Price']*indexes[k]['Float']
        indexes[k]['Weight'] = indexes[k]['Mkt Cap'] / indexes[k]['Mkt Cap'].sum()
        indexes[k]['Equal'] = 1.00 / (len(indexes[k]))
        indexes[k]['AWF'] = indexes[k]['Equal'] / indexes[k]['Weight']
        indexes[k]['Adj Mkt Cap'] = indexes[k]['Mkt Cap'] * indexes[k]['AWF']
        indexes[k]['Adj Weight'] = indexes[k]['Adj Mkt Cap'] / indexes[k]['Adj Mkt Cap'].sum()
        indexes[k]['Adj Float'] = indexes[k]['Float'] * indexes[k]['AWF']
        sum_weight = indexes[k]['Weight'].sum()
        sum_equal_weight = indexes[k]['Adj Weight'].sum()
        #print k, sum_weight, sum_equal_weight, indexes[k]['Weight'].max(), indexes[k]['Weight'].min()
        new_constants.loc[k,('Divisor')] = old_constants.loc[k,('Divisor')] *        (indexes[k]['Mkt Cap'].sum() / old_constants.loc[k,('Mkt Cap')])
        new_constants.loc[k,('Adj Divisor')] = old_constants.loc[k,('Adj Divisor')] *        (indexes[k]['Adj Mkt Cap'].sum() / old_constants.loc[k,('Adj Mkt Cap')])
        new_constants.loc[k,('USD')] = indexes[k]['Mkt Cap'].sum() / new_constants.loc[k,('Divisor')]
        new_constants.loc[k,('Adj USD')] = indexes[k]['Adj Mkt Cap'].sum() / new_constants.loc[k,('Adj Divisor')]
        new_constants.loc[k,('BTC')] = new_constants.loc[k,('USD')] / bitcoin
        new_constants.loc[k,('Adj BTC')] = new_constants.loc[k,('Adj USD')] / bitcoin


# In[16]:


#check for ticker issues
for k in indexes:
    #print indexes[k]['Price']
    if (indexes[k]['Price'] < 0.0001).any() == True:
            print (k, 'missing ticker!' , indexes[k]['Price'])


# In[17]:


#these should always almost match old prod index values
new_constants


# In[18]:


for k in indexes:
    if (k == 'Currency' or k == 'Platform' or k == 'Application'):
        indexes_float_change[k]['Float Change'] = indexes[k]['Adj Float'] - indexes_old[k]['Adj Float']
        indexes_float_change[k]['Float Change Percent'] = (indexes[k]['Adj Float'] / indexes_old[k]['Adj Float']) - 1
    else:
        indexes_float_change[k]['Float Change'] = indexes_new[k]['Float'] - indexes_old[k]['Float']
        indexes_float_change[k]['Float Change Percent'] = (indexes_new[k]['Float'] / indexes_old[k]['Float']) - 1


# In[19]:


#change file path below!
#only when actually running rebalance - change this to StaticValues and point to prod constants dir
with open(STATIC_CONSTANTS_JAVA_FILE,'r') as constants:
    lines = constants.readlines()


# In[20]:


ten_divisor = new_constants.loc['10']['Divisor'].astype(str)
ten_divisor_even = new_constants.loc['10']['Adj Divisor'].astype(str)
twenty_divisor = new_constants.loc['20']['Divisor'].astype(str)
twenty_divisor_even = new_constants.loc['20']['Adj Divisor'].astype(str)
forty_divisor = new_constants.loc['40']['Divisor'].astype(str)
forty_divisor_even = new_constants.loc['40']['Adj Divisor'].astype(str)
total_divisor = new_constants.loc['Total']['Divisor'].astype(str)
total_divisor_even = new_constants.loc['Total']['Adj Divisor'].astype(str)
eth_divisor = new_constants.loc['ETH']['Divisor'].astype(str)
eth_divisor_even = new_constants.loc['ETH']['Adj Divisor'].astype(str)
currency_divisor = new_constants.loc['Currency']['Divisor'].astype(str)
platform_divisor = new_constants.loc['Platform']['Divisor'].astype(str)
application_divisor = new_constants.loc['Application']['Divisor'].astype(str)


# In[21]:


#DO NOT TOUCH! Extremely sensitive and hard to catch errors!
new_csv_file_lines = {8 , 9 , 12 , 18 , 24 , 30 , 36 , 41 , 46 , 51 , 16 , 17,22,23,28,29,34,35,40,45,50}
indexes_csv = {'10' , '20' , '40' , 'ETH' , 'Total' , 'Currency' , 'Platform' , 'Application'}
for l in new_csv_file_lines:
    #print l
    if l == 12:
        lines[l] = '  public static final String MKT_CAP_FILE = "business_rules/10_'+new_month+'_rebal.csv";\n'
    elif l == 8:
        lines[l] = '  public static final double DIVISOR_TEN = '+ten_divisor+';\n'
    elif l == 9:
        lines[l] = '  public static final double DIVISOR_EVEN_TEN = '+ten_divisor_even+';\n'
    elif l == 16:
        lines[l] = '  public static final double DIVISOR_20 = '+twenty_divisor+';\n'
    elif l == 17:
        lines[l] = '  public static final double DIVISOR_EVEN_20 = '+twenty_divisor_even+';\n'
    elif l == 22:
        lines[l] = '  public static final double DIVISOR_40 = '+forty_divisor+';\n'
    elif l == 23:
        lines[l] = '  public static final double DIVISOR_EVEN_40 = '+forty_divisor_even+';\n'
    elif l == 34:
        lines[l] = '  public static final double DIVISOR_TOTAL = '+total_divisor+';\n'
    elif l == 35:
        lines[l] = '  public static final double DIVISOR_EVEN_TOTAL = '+total_divisor_even+';\n'
    elif l == 28:
        lines[l] = '  public static final double DIVISOR_ETHER = '+eth_divisor+';\n'
    elif l == 29:
        lines[l] = '  public static final double DIVISOR_EVEN_ETHER = '+eth_divisor_even+';\n'
    elif l == 40:
        lines[l] = '  public static final double DIVISOR_CURRENCY = '+currency_divisor+';\n'
    elif l == 45:
        lines[l] = '  public static final double DIVISOR_PLATFORM = '+platform_divisor+';\n'
    elif l == 50:
        lines[l] = '  public static final double DIVISOR_APPLICATION = '+application_divisor+';\n'
    elif l == 18:
        lines[l] = '  public static final String MKT_CAP_FILE_20 = "business_rules/20_'+new_month+'_rebal.csv";\n'
    elif l == 24:
        lines[l] = '  public static final String MKT_CAP_FILE_40 = "business_rules/40_'+new_month+'_rebal.csv";\n'
    elif l == 30:
        lines[l] = '  public static final String MKT_CAP_FILE_ETHER = "business_rules/ETH_'+new_month+'_rebal.csv";\n'
    elif l == 36:
        lines[l] = '  public static final String MKT_CAP_FILE_TOTAL = "business_rules/Total_'+new_month+'_rebal.csv";\n'
    elif l == 41:
        lines[l] = '  public static final String MKT_CAP_FILE_CURRENCY = "business_rules/Currency_'+new_month+'_rebal.csv";\n'
    elif l == 46:
        lines[l] = '  public static final String MKT_CAP_FILE_PLATFORM = "business_rules/Platform_'+new_month+'_rebal.csv";\n'
    elif l == 51:
        lines[l] = '  public static final String MKT_CAP_FILE_APPLICATION = "business_rules/Application_'+new_month+'_rebal.csv";\n'


# In[22]:


#change file path below!
#have to change this to point to production constants and switch to StaticValues.java
with open(STATIC_CONSTANTS_JAVA_FILE,'w') as constants:
    constants.writelines( lines )
constants.close()


# In[23]:


#change file path below!
#change below twice to point to prod business_rules directory!
new_constants.to_csv('../index-data/src/main/resources/business_rules/All_'+new_month+'.csv')
indexes_final = dict.fromkeys(list_indexes)
for k in indexes:
    if (k == 'Currency' or k == 'Platform' or k == 'Application'):
        indexes_final[k] = indexes[k].drop(['Float' , 'Price' , 'Mkt Cap' , 'Weight' , 'Weight Cap' , 'Weight1' , 'Weight_step2' , 'Weight2'         , 'Weight_step3' , 'Weight3' , 'Weight_step4' , 'Weight4' , 'AWF' , 'Adj Mkt Cap' , 'Adj Weight'],axis=1)
    else:
        indexes_final[k] = indexes[k].drop(['Price' , 'Mkt Cap' , 'Weight' , 'Equal' , 'AWF' , 'Adj Mkt Cap' , 'Adj Weight'],axis=1)
    #change below to point to prod business_rules!!
    indexes_final[k].to_csv('../index-data/src/main/resources/business_rules/'+k+'_'+new_month+'_rebal.csv' , header = False)


# In[24]:


weights_API_list = ['TEN' , 'TWENTY' , 'FORTY' , 'TOTAL' , 'ETHEREUM' , 'CURRENCY' , 'PLATFORM' , 'APPLICATION']
endPoint = "https://www.bletchleyindexes.com/api/weight";
for k in weights_API_list:
    data = {'index':k}
    old_weights[k] = pd.DataFrame(columns=['Weight'])
    response = requests.post(endPoint, json=data).text
    old_weights[k]['Weight'] = pd.read_json(response, typ='series' , encoding='utf-8')


# In[25]:


list_indexes = ['10' , '20' , '40' , 'Total' , 'ETH' , 'Currency' , 'Platform' , 'Application']
weights_API_list = ['TEN' , 'TWENTY' , 'FORTY' , 'TOTAL' , 'ETHEREUM' , 'CURRENCY' , 'PLATFORM' , 'APPLICATION']
weights_master = dict.fromkeys(list_indexes)
old_weights['10'] = old_weights['TEN']
old_weights['20'] = old_weights['TWENTY']
old_weights['40'] = old_weights['FORTY']
old_weights['ETH'] = old_weights['ETHEREUM']
old_weights['Currency'] = old_weights['CURRENCY']
old_weights['Platform'] = old_weights['PLATFORM']
old_weights['Application'] = old_weights['APPLICATION']
old_weights['Total'] = old_weights['TOTAL']


# In[26]:


sectors = ['Application' , 'Platform' , 'Currency']
for k in indexes:
    new_weights[k] = pd.DataFrame()
    if k in sectors:
        new_weights[k]['Weight'] = indexes[k]['Adj Weight']
    else:
        new_weights[k]['Weight'] = indexes[k]['Weight']
turnover_summary = pd.DataFrame(index=list_indexes , columns=['Turnover'])
for k in indexes:
    #print k
    adds = indexes_new[k].index.difference(indexes_old[k].index).values
    deletes = indexes_old[k].index.difference(indexes_new[k].index).values
    turnover[k] = pd.DataFrame(columns=['Turnover'])
    for t in adds:
        new_ticker = {'Weight': [0]}
        new_add = pd.DataFrame(data = new_ticker)
        old_weights[k] = old_weights[k].append([new_add])
        old_weights[k].index = old_weights[k].index[:-1].append(pd.Index([t]))
    for t in deletes:
        new_ticker = {'Weight': [0]}
        new_delete = pd.DataFrame(data = new_ticker)
        new_weights[k] = new_weights[k].append([new_delete])
        new_weights[k].index = new_weights[k].index[:-1].append(pd.Index([t]))
    new_weights[k] = new_weights[k].fillna(0)
    new_weights_clean[k] = pd.DataFrame(columns=['Weight'])
    new_weights_clean[k]['Weight'] = new_weights[k]['Weight']
    old_weights[k] = old_weights[k].fillna(0)
    old_weights_clean[k] = old_weights[k]
    turnover[k]['Turnover'] = (new_weights_clean[k]['Weight'] - old_weights_clean[k]['Weight']).abs()
    weights_master[k] = pd.DataFrame()
    weights_master[k]['New Weight'] = new_weights_clean[k]['Weight']
    weights_master[k]['Old Weight'] = old_weights_clean[k]['Weight']
    weights_master[k]['Turnover'] = turnover[k]['Turnover']
    weights_master[k] = weights_master[k].fillna(0)
    turnover_summary.loc[k,('Turnover')] = turnover[k]['Turnover'].abs().sum()/2


# In[27]:


#change file path below!
upload_weights = pd.DataFrame()
upload_weights = pd.concat([turnover_summary ])
upload_floats = pd.DataFrame()
for k in indexes:
    index_name = pd.DataFrame(columns = ['New Weight' , 'Old Weight' , 'Turnover'])
    index_name.set_index = (k+' Index')
    index_name.loc[k] = ['New Weight' , 'Old Weight' , 'Turnover']
    upload_weights = upload_weights.append(index_name)
    upload_weights = pd.concat([upload_weights , weights_master[k]])
    index_float_name = pd.DataFrame(columns = ['Float Change' , 'Float Change Percent'])
    index_float_name.set_index = (k+' Index')
    index_float_name.loc[k] = ['Float Change' , 'Float Change Percent']
    upload_floats = upload_floats.append(index_float_name)
    upload_floats = pd.concat([upload_floats , indexes_float_change[k]])
#change to point to prod static/weights directory!
upload_weights.to_csv('../index-view/src/main/resources/static/weights/'+new_month+'_2018.csv')
upload_floats.to_csv('../index-view/src/main/resources/static/weights/'+new_month+'_floats_2018.csv')

