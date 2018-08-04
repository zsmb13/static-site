package co.zsmb.staticsite.data

import org.springframework.data.mongodb.repository.MongoRepository

interface AnalyticsEventRepository : MongoRepository<AnalyticsEvent, String>
