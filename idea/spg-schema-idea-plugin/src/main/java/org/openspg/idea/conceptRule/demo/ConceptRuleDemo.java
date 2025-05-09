package org.openspg.idea.conceptRule.demo;

public class ConceptRuleDemo {

    public static String getHighlighterText() {
        return """
            namespace SupplyChain
            
            `TaxOfProdEvent`/`价格上涨`:
                rule: [[
                    Define (e:ProductChainEvent)-[p:belongTo]->(o:`TaxOfProdEvent`/`价格上涨`) {
                        Structure {
                        }
                        Constraint {
                            R1: e.index == '价格'
                            R2: e.trend == '上涨'
                        }
                    }
                ]]
            
            `TaxOfCompanyEvent`/`成本上涨`:
                rule: [[
                    Define (e:CompanyEvent)-[p:belongTo]->(o:`TaxOfCompanyEvent`/`成本上涨`) {
                        Structure {
                        }
                        Constraint {
                            R1: e.index == '成本'
                            R2: e.trend == '上涨'
                        }
                    }
                ]]
            `TaxOfCompanyEvent`/`利润下跌`:
                 rule: [[
                     Define (e:CompanyEvent)-[p:belongTo]->(o:`TaxOfCompanyEvent`/`利润下跌`) {
                         Structure {
                         }
                         Constraint {
                             R1: e.index == '利润'
                             R2: e.trend == '下跌'
                         }
                     }
                 ]]
            
            `TaxOfProdEvent`/`价格上涨`:TaxOfCompanyEvent/`成本上涨`
                rule: [[
                    Define (s:`TaxOfProdEvent`/`价格上涨`)-[p:leadTo]->(o:`TaxOfCompanyEvent`/`成本上涨`) {
                        Structure {
                            (s)-[:subject]->(prod:Product)-[:hasSupplyChain]->(down:Product)<-[:product]-(c:Company)
                        }
                        Constraint {
                        eventName = concat(c.name, "成本上升事件")
                        }
                        Action {
                            downEvent = createNodeInstance(
                                type=CompanyEvent,
                                value = {
                                    subject=c.id
                                    name=eventName
                                    trend="上涨"
                                    index="成本"
                                }
                            )
                            createEdgeInstance(
                                src=s,
                                dst=downEvent,
                                type=leadTo,
                                value={}
                            )
                        }
                    }
                ]]
            
            `TaxOfCompanyEvent`/`成本上涨`:TaxOfCompanyEvent/`利润下跌`
                rule: [[
                    Define (s:`TaxOfCompanyEvent`/`成本上涨`)-[p:leadTo]->(o:`TaxOfCompanyEvent`/`利润下跌`) {
                        Structure {
                            (s)-[:subject]->(c:Company)
                        }
                        Constraint {
                            eventName = concat(c.name, "利润下跌事件")
                        }
                        Action {
                            downEvent = createNodeInstance(
                                type=CompanyEvent,
                                value={
                                    subject=c.id
                                    name=eventName
                                    trend="下跌"
                                    index="利润"
                                }
                            )
                            createEdgeInstance(
                                src=s,
                                dst=downEvent,
                                type=leadTo,
                                value={}
                            )
                        }
                    }
                ]]""";
    }

    public static String getCodeStyleText() {
        return """
            namespace RiskMining
            
            `TaxOfRiskApp`/`赌博应用`:
                rule: [[
                    Define (s:App)-[p:belongTo]->(o:`TaxOfRiskApp`/`赌博应用`) {
                        Structure {
                            (s)
                        }
                        Constraint {
                            R1("风险标记为赌博"): s.riskMark like "%赌博%"
                        }
                    }
                ]]
            
            `TaxOfRiskUser`/`赌博App开发者`:
                rule: [[
                    Define (s:Person)-[p:belongTo]->(o:`TaxOfRiskUser`/`赌博App开发者`) {
                        Structure {
                            (s)-[:developed]->(app:`TaxOfRiskApp`/`赌博应用`)
                        }
                        Constraint {
                        }
                    }
                ]]
            
            `TaxOfRiskUser`/`赌博App老板`:
                rule: [[
                    Define (s:Person)-[p:belongTo]->(o:`TaxOfRiskUser`/`赌博App老板`) {
                        Structure {
                            (s)-[:release]->(a:`TaxOfRiskApp`/`赌博应用`),
                            (u:Person)-[:developed]->(a),
                            (s)-[:fundTrans]->(u)
                        }
                        Constraint {
                        }
                    }
                ]]""";
    }

}
