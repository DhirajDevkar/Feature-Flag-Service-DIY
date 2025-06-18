
import React, { useEffect, useState } from 'react';
import { VerticalTimeline, VerticalTimelineElement } from 'react-vertical-timeline-component';
import 'react-vertical-timeline-component/style.min.css';
const getTimeline = async () => {
  return [
    {
      name: "American Family Insurance",
      title: "Tech Lead / Lead Application Development Engineer",
      dateRange: "Feb 2022 – Present",
      techStack: "Java, Spring Boot, AWS, Docker, DynamoDB, Elasticsearch",
      summaryPoints: [
        "Led a team of 5 engineers building scalable Sales API platforms for Home and Auto insurance.",
        "Architected 10+ microservices, including a shopping cart system that reduced latency by 80%.",
        "Championed Agile delivery through sprint planning and stakeholder alignment.",
        "Migrated Redis to AWS DynamoDB, integrated Elasticsearch, and implemented Docker-based feature flag testing."
      ]
    },
    {
      name: "American Family Insurance",
      title: "Application Development Senior Engineer",
      dateRange: "Aug 2021 – Feb 2022",
      techStack: "Apple Pay, Pricing Engine, Spring Boot, Monitoring, CI/CD",
      summaryPoints: [
        "Delivered key integrations including Apple Pay, Multirate pricing engine, and Bookroll.",
        "Built centralized dashboards and on-call alerting APIs for proactive platform monitoring."
      ]
    },
    {
      name: "American Family Insurance",
      title: "Application Development Engineer",
      dateRange: "Aug 2019 – Aug 2021",
      techStack: "Java, OAuth2, JWT, Redis, AWS Secrets Manager",
      summaryPoints: [
        "Built a secure token microservice cutting API load by 90%.",
        "Created 'Lunch and Learn' to promote knowledge sharing within the team."
      ]
    }
  ];
};
import { TimelineItem } from '../types';
import { FaBriefcase } from 'react-icons/fa';
import { Fade } from 'react-awesome-reveal';

const Professional = () => {
  const [timelineData, setTimelineData] = useState<TimelineItem[]>([]);

  useEffect(() => {
    async function fetchTimeline() {
      const data = await getTimeline();
      setTimelineData(data);
    }

    fetchTimeline();
  }, []);

  return (
    <div style={{ background: '#000', color: '#fff', padding: '2rem' }}>
      <h1 style={{ textAlign: 'center', marginBottom: '3rem' }}>Work Experience & Education Timeline</h1>
      <VerticalTimeline>
        {timelineData.map((item, index) => (
          <Fade triggerOnce direction="up" key={index}>
            <VerticalTimelineElement
              className="vertical-timeline-element--work"
              contentStyle={{
                background: index % 2 === 0 ? '#1a1a1a' : '#292929',
                color: '#fff',
                boxShadow: '0 0 15px rgba(229, 9, 20, 0.4)'
              }}
              contentArrowStyle={{ borderRight: '7px solid #e50914' }}
              date={item.dateRange}
              iconStyle={{ background: '#e50914', color: '#fff' }}
              icon={<FaBriefcase />}
            >
              <h3 className="vertical-timeline-element-title" style={{ fontWeight: 'bold' }}>{item.title}</h3>
              <h4 className="vertical-timeline-element-subtitle" style={{ color: '#aaa' }}>{item.name}</h4>
              <p><strong>🔧 {item.techStack}</strong></p>
              <ul style={{ paddingLeft: '1.2rem' }}>
                {item.summaryPoints.map((point, i) => (
                  <li key={i} style={{ marginBottom: '0.5rem' }}>{point}</li>
                ))}
              </ul>
            </VerticalTimelineElement>
          </Fade>
        ))}
      </VerticalTimeline>
    </div>
  );
};

export default Professional;
